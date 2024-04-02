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
            steps {
                sh "mvn package -DskipTests"
            }
        }
    }
}
