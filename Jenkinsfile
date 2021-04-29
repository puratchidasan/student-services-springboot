pipeline {
    agent any
	environment {
			VERSION = readMavenPom().getVersion()
	}
	parameters {
			booleanParam defaultValue: false, description: 'Do you wish to build quick?(build=true/false)', name: 'Quick_Build'
	        booleanParam defaultValue: false, description: 'Do you wish to skip the test?(DskipTests=true/false)', name: 'Skip_Testing'
			booleanParam defaultValue: false, description: 'Do you wish to generate the test reports?', name: 'Generate_Test_Reports'
			booleanParam defaultValue: false, description: 'Do you wish to generate the sonar reports?', name: 'Generate_Sonar_Reports'
			booleanParam defaultValue: false, description: 'Do you wish to generate the cucumber reports?', name: 'Generate_Cucumber_Reports'
			string(defaultValue: "develop", description: 'Which branch?', name: 'GIT_BRANCH_NAME')
       }
    stages {
        stage('CHECKOUT') { 
            steps { 
               git branch: "${params.GIT_BRANCH_NAME}", credentialsId: 'MY_GIT_Credentials', url: 'https://github.com/puratchidasan/student-services-springboot.git'
			   echo 'Code has been checkedout successfully.'
			   sh "git branch -a"
			   sh "git remote -v"
			    // Clean any locally modified files and ensure we are actually on origin/develop
				// as a failed release could leave the local workspace ahead of origin/develop
				sh "git clean -f && git reset --hard origin/develop"
				script {
				   sh "which java"
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
					maven: 'M3') {
					sh "mvn versions:set -DnewVersion=${VERSION} -DprocessAllModules -DgenerateBackupPoms=false"
					script {
					    // Run the maven build
					    if(params.Skip_Testing){
					        sh "mvn clean -DskipTests=true prepare-package install"
					    }
					    if (params.Generate_Cucumber_Reports){
					        sh "mvn clean test verify"
					    }
						if (params.Quick_Build){
					        sh "mvn clean -DskipTests=true prepare-package install"
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
						sh "git remote -v"
					}
				}				
			}		
		}
		stage('SONAR') {
			environment {
				scannerHome = tool 'MY_SONAR'
			}
			when {
                expression { return params.Generate_Sonar_Reports}
            }
			steps {
				withSonarQubeEnv('SONARQUBE') {
					sh "/var/jenkins_home/sonarscanner/bin/sonar-scanner scan -Dsonar.login=${SONAR_LOGIN}"
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
  }
}