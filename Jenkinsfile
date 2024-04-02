pipeline {
    agent any

    tools {
        maven "maven-3.9.6"
    }

    environment {
        GYMAPP_DB_URL = credentials('GYMAPP_DB_URL')
        GYMAPP_DB_USER = credentials('GYMAPP_DB_USER')
        GYMAPP_DB_PASSWORD = credentials('GYMAPP_DB_PASSWORD')
    }

    stages {
        stage('Test') {
            steps {
                sh "mvn clean test -DDB_URL=$GYMAPP_DB_URL -DDB_USER=$GYMAPP_DB_USER -DDB_PASSWORD=$GYMAPP_DB_PASSWORD"
            }
        }

        stage('Package') {
            when {
                branch 'main'
            }
            steps {
                sh "mvn package -DskipTests"
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    def appJarPath = sh(script: "ls target/gym-app-*.jar | head -n 1", returnStdout: true).trim()
                    def appJarFilename = sh(script: "basename $appJarPath", returnStdout: true).trim()
                    env.APP_JAR_PATH = appJarPath
                    env.APP_JAR_FILENAME = appJarFilename

                    withCredentials([
                            string(credentialsId: 'GYMAPP_S3', variable: 'GYMAPP_S3'),
                            string(credentialsId: 'GYMAPP_MAIN_EC2_IP', variable: 'GYMAPP_MAIN_EC2_IP'),
                            sshUserPrivateKey(
                                    credentialsId: 'GYMAPP_MAIN_EC2_SSH',
                                    keyFileVariable: 'GYMAPP_MAIN_EC2_SSH_KEY',
                                    usernameVariable: 'GYMAPP_MAIN_EC2_SSH_USER'
                            )
                    ]) {
                        def remote = [:]
                        remote.name = 'gym-app-main-ec2'
                        remote.host = GYMAPP_MAIN_EC2_IP
                        remote.user = GYMAPP_MAIN_EC2_SSH_USER
                        remote.identityFile = GYMAPP_MAIN_EC2_SSH_KEY
                        remote.allowAnyHosts = true;

                        sshCommand(remote: remote, command: '''
                            pid=$(sudo lsof -t -i :8080)
                            if [ -n "$pid" ]; then
                                sudo kill $pid
                            fi
                        ''')

                        sh "aws s3 cp $APP_JAR_PATH s3://$GYMAPP_S3/releases/$APP_JAR_FILENAME"

                        sshCommand(remote: remote, command: "aws s3 cp s3://$GYMAPP_S3/releases/$APP_JAR_FILENAME gym-app/$APP_JAR_FILENAME")
                        sshCommand(remote: remote, command: "nohup java -DDB_URL=$GYMAPP_DB_URL -DDB_USER=$GYMAPP_DB_USER -DDB_PASSWORD=$GYMAPP_DB_PASSWORD -jar gym-app/$APP_JAR_FILENAME > /dev/null 2>&1 &")
                    }
                }
            }
        }
    }
}
