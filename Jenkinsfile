pipeline {
    agent any

    parameters {
        string(name: 'BROWSERS', defaultValue: 'chrome,firefox,edge', description: 'Comma-separated browsers')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run in headless mode?')
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

                    for (b in browsers) {
                        def browserName = b.trim()
                        branches[browserName] = {
                            stage("Tests on ${browserName}") {
                                withEnv(["HEADLESS=${params.HEADLESS}", "BROWSER=${browserName}"]) {
                                    bat "mvn clean test -DsuiteXmlFile=testng.xml -Dbrowser=${browserName} -Dheadless=${params.HEADLESS}"
                                }
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
            echo 'All browsers finished!'
        }
    }
}
