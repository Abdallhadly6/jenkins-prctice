pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'Maven-3'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Abdallhadly6/jenkins-prctice.git'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                bat '''
                mvn clean test -DsuiteXmlFile=testng.xml
                '''
            }
        }
    }

    post {
        always {
            echo 'Selenium execution finished'
        }
    }
}
