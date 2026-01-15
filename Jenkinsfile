pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'jdk17'   // make sure this name exists in Jenkins tools
    }

    parameters {
        string(
            name: 'BROWSERS',
            defaultValue: 'chrome,firefox,edge',
            description: 'Comma-separated browsers to run'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests headless'
        )
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Run Selenium Tests') {
            steps {
                script {
                    def browsers = params.BROWSERS.split(',')
                    def branches = [:]

                    for (String b : browsers) {
                        def browser = b.trim()

                        branches[browser] = {
                            stage("Tests on ${browser}") {
                                bat """
                                    mvn clean test ^
                                    -DsuiteXmlFile=testng.xml ^
                                    -Dbrowser=${browser} ^
                                    -Dheadless=${params.HEADLESS}
                                """
                            }
                        }
                    }

                    parallel branches
                }
            }
        }
    }

    post {
        always {
            echo "All browsers finished!"
        }
    }
}
