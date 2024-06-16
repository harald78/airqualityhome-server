pipeline {
    agent any
    environment {
        HOME = "${env.WORKSPACE}"
    }
    stages {
        stage('Build') {
            when {
              not {
                anyOf {
                    tag 'PROD-RELEASE'
                    branch 'master'
                }
              }
            }
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build prod') {
            when {
                anyOf {
                    tag 'PROD-RELEASE'
                    branch 'master'
                }
            }
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
              when {
                  anyOf {
                      tag 'PROD-RELEASE'
                      branch 'master'
                  }
              }
             steps {
                sh 'cp -r ./target/*.jar /opt/server/app.jar'
                sh 'cp ./Dockerfile /opt/server/Dockerfile'
                sh 'cp ./compose.app.yaml /opt/server/compose.app.yaml'
                sh 'cd /opt/server'
                sh 'ls -la'
           }
        }
    }
}
