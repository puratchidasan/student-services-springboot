pipeline { 
    agent any
	environment {
			VERSION = readMavenPom().getVersion()
	}
	parameters {
	        booleanParam defaultValue: false, description: 'Do you wish to skip the test?(DskipTests=true/false)', name: 'Skip_Testing'
			booleanParam defaultValue: false, description: 'Do you wish to generate the test reports?', name: 'Generate_Test_Reports'
			booleanParam defaultValue: false, description: 'Do you wish to generate the sonar reports?', name: 'Generate_Sonar_Reports'
			booleanParam defaultValue: false, description: 'Do you wish to generate the cucumber reports?', name: 'Generate_Cucumber_Reports'
			string(defaultValue: "develop", description: 'Which branch?', name: 'TFS_GIT_BRANCH_NAME')
       }
    stages {
        stage('CHECKOUT') { 
            steps { 
               //git branch: "${params.TFS_GIT_BRANCH_NAME}", credentialsId: 'B37442-LOGIN-FOR-TESTING', url: 'https://tfs.am.echonet/DefaultCollection/_git/student-services'
			   echo 'Code has been checkedout successfully.'
			   sh "git branch -a"
			   sh "git remote -v"
			    // Clean any locally modified files and ensure we are actually on origin/develop
				// as a failed release could leave the local workspace ahead of origin/develop
				//sh "git clean -f && git reset --hard origin/develop"
				script {
				echo "VERSION: ${VERSION}"
					// we want to pick up the version from the pom
					def pom = readMavenPom file: 'pom.xml'
					def version = pom.version.replace("-SNAPSHOT", ".${currentBuild.number}-SNAPSHOT")
					echo "VERSION: ${version}"
					VERSION = version
				}
            }
        }
		stage('BUILD') { 
			steps { 
					withMaven(
					// Maven installation declared in the Jenkins "Global Tool Configuration"
					maven: 'M3',
					// Maven settings.xml file defined with the Jenkins Config File Provider Plugin
					// Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
					mavenSettingsConfig: 'AMCIC-MAVEN-CONFIG',
					mavenLocalRepo: '.repository') {
					sh "mvn versions:set -DnewVersion=${VERSION} -DprocessAllModules -DgenerateBackupPoms=false"
					script {
					    // Run the maven build
					    if(params.Skip_Testing){
					        sh "mvn clean -DskipTests=true prepare-package install"
					    }
					    if (params.Generate_Cucumber_Reports){
					        sh "mvn clean test verify"
					    }
					}
				} 
			}
		}
		stage('TESTING'){
			parallel {
				stage('TEST-REPORTS'){
            		 when {
                        expression { return params.Generate_Test_Reports}
                    }
								steps {
										publishCoverage adapters: [jacocoAdapter('target\\surefire-reports\\jacoco\\jacoco.xml')], sourceFileResolver: sourceFiles('NEVER_STORE')
										publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target\\surefire-reports\\jacoco', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: 'AMCIC-TOOLCHAIN'])
										junit allowEmptyResults: true, keepLongStdio: true, testDataPublishers: [[$class: 'AttachmentPublisher']], testResults: 'target/surefire-reports/TEST*.xml'				
								}
							}
				stage('JUNIT-CODE-COVERAGE-TFS') {
				    when {
                        expression { return params.Generate_Test_Reports}
                    }
					steps{
						step([$class: 'TeamCollectResultsPostBuildAction', 
									requestedResults: [
											[includes: 'target\\surefire-reports\\TEST*.xml', teamResultType: 'JUNIT'],
											[includes: 'target\\surefire-reports\\jacoco\\jacoco.xml', teamResultType: 'JACOCO']
									]
							])
					}
				}				
			}		
		}
		stage('SONAR') {
			environment {
				scannerHome = tool 'SonarQubeScanner'
			}
			when {
                expression { return params.Generate_Sonar_Reports}
            }
			steps {
				withSonarQubeEnv('SONARQUBE') {
					sh "/soft/jenkins/home/sonar-scanner/bin/sonar-scanner scan -Dsonar.login=${SONAR_USERNAME} -Dsonar.password=${SONAR_PASSWORD}"
				}
				sleep(60)
                echo 'Checking quality gate...'
                script {
                    timeout(time: 1, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        echo qg.status
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
		}		
		stage('DOWNLOAD') {
				   steps {
				   sh 'rm -f /soft/jenkins/home/student-services-${VERSION}.jar'
					  script { 
						 def server = Artifactory.server 'ARTIFACTORY_PRO'
						 def downloadSpec = """{
							"files": [{
							   "pattern": "amcic-snapshot-local/student-services-${VERSION}.jar",
							   "target": "/soft/jenkins/home/"
							}]
						 }"""
						 server.download(downloadSpec) 
				}
			}
		}
		stage ('DEPLOY') {
			steps {
				build job: 'RunSpringBootApplication',parameters: [[$class: 'StringParameterValue', name: 'VERSION', value: "${VERSION}"]]					
				}
		}						
	}
	post {
    always {
        archiveArtifacts artifacts: '**/*.sh', fingerprint: true
         // Cucumber report plugin
		cucumber fileIncludePattern: '**/*.json', sortingMethod: 'ALPHABETICAL'
		
        
       mail to: 'LIST.AM.TOOLCHAINAMCIC@bnpparibas.com',
          subject: "AMCIC-TOOLCHAIN-JENKINS-Status of pipeline:${currentBuild.currentResult}",
		  body: """
			<html>
				<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
				<style>
				.bnp_label {
					color: #00965e;
				}
				.bnp_groupBy table, th, tr, td {
					border-collapse: collapse;
				}

				.bnp_groupBy thead {
					background-color: #00965e
				}

				.bnp_groupBy th {
					background-color: #00965e
				}

				.bnp_groupBy td {
					border-collapse: collapse;
				}

				.bnp_groupBy tr:nth-child(even) {
					background-color: #eee
				}

				.bnp_groupBy tr:nth-child(odd) {
					background-color: #fff border-top: 1px solid black;
				}
				</style>
				</head>
				<body>
					<table style="width: 100%;">
						<tbody>
							<tr align="center">
								<td>
									<table
										style="border-top: 3px solid #00965E; border-bottom: 3px solid #00965E; width: 800px;">
										<tbody>
											<tr>
												<td
													style="height: 68px; width =243px; text-align: left; padding: 0px 0px 5px 0px;"><img
													align="baseline" src="https://jenkins-int.am.staging.echonet/student-service/bnp_image.jpeg" alt="" title=""
													width="243px" height="68px"></td>
											</tr>
											<tr>
												<td style="text-align: left; padding: 0px; font-size: 16px;">
													<p></p>
													<p>Dear colleague,</p>
													<p>
														<b>Jenkins Build Details</b>
													</p>
													<div>
														<span class="bnp_label">Name of the build : </span>${env.JOB_NAME}
													</div>
													<div>
														<span class="bnp_label">Build Number : </span>${currentBuild.number}
													</div>
													<div>
														<span class="bnp_label">Build Status : </span>${currentBuild.currentResult}
													</div>
													<div>
														<span class="bnp_label">Duration of the build : </span>${currentBuild.durationString}
													</div>
													<div>
														<p>You can view all the details of the build by following
															the link below</p>
														<table
															style="background-color: #00965E; color: #ffffff; align: left; width: auto; max-width: 200px;">
															<tbody>
																<tr>
																	<td><a href="${env.BUILD_URL}"
																		style="color: #ffffff; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">Take
																			me to the jenkins build</a></td>
																</tr>
															</tbody>
														</table>
													</div>

													<p>&nbsp;</p>
													<p>
													<a href="${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME}/detail/${env.JOB_NAME}/${currentBuild.number}/pipeline"
													style="color: blue; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">Jenkins Pipeline</a>
													
													|
													<a href="${env.BUILD_URL}testReport/"
													style="color: blue; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">Test Reports</a>
													
													|
													 <a href="https://sonarqube.am.echonet/dashboard?id=springboot"
													style="color: blue; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">Sonar Reports</a>
													|
													 <a href="${env.JOB_URL}coverage"
													style="color: blue; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">Jacoco Reports</a>
													|
													<a href="${env.JOB_URL}HTML_20Report/"
													style="color: blue; font-size: 16px; text-decoration: none; border-radius: 3px; padding: 0px;">HTML Reports</a>
												
													</p> </td>
											</tr>									
																						
											<tr>
												<td
													style="height: 50px; text-align: left; font-size: 16px; padding: 35px 0px 5px 0px; padding-left: 0px;">
													<p>
														AMCIC Toolchain,IT4IT <br>DevOps Practise<br> BNP
														Paribas Asset Management, Belgium
													</p>
												</td>
											</tr>
										</tbody>
									</table>
								</td>
								<td align="center"></td>
							</tr>
						</tbody>
					</table>
					<div>&nbsp;</div>
				</body>
				</html>
          """,
		  mimeType: "text/html"
    }
  }
}