pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    parameters {
        string(
            name: 'BROWSERS',
            defaultValue: 'chrome',
            description: 'Enter browsers to run, comma-separated (chrome,firefox,edge,safari)'
        )
        choice(
            name: 'HEADLESS',
            choices: ['true','false'],
            description: 'Run browsers in headless mode?'
        )
        string(
            name: 'EMAIL_TO',
            defaultValue: 'qa-team@example.com',
            description: 'Comma-separated primary recipients'
        )
        string(
            name: 'EMAIL_CC',
            defaultValue: '',
            description: 'Comma-separated CC recipients'
        )
        string(
            name: 'EMAIL_BCC',
            defaultValue: '',
            description: 'Comma-separated BCC recipients'
        )
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
                script {
                    def browsers = params.BROWSERS.split(',')
                    def parallelBranches = [:]

                    for (b in browsers) {
                        def browserName = b.trim()
                        parallelBranches[browserName] = {
                            runTestsForBrowser(browserName)
                        }
                    }

                    parallel parallelBranches
                }
            }
        }
    }
}

// helper function for parallel browser runs
def runTestsForBrowser(String browserName) {
    stage("Tests on ${browserName}") {
        withEnv(["BROWSER=${browserName}", "HEADLESS=${params.HEADLESS}"]) {
            // run Maven tests with system properties
            bat "mvn clean test -DsuiteXmlFile=testng.xml -Dbrowser=${browserName} -Dheadless=${params.HEADLESS}"

            // ensure folder exists
            bat "if not exist test-reports mkdir test-reports"

            // archive results for this browser
            bat "powershell Compress-Archive -Path target/surefire-reports/* -DestinationPath test-reports-${browserName}.zip"

            archiveArtifacts artifacts: "test-reports-${browserName}.zip", fingerprint: true

            // send email
            emailext(
                subject: "Selenium Tests on ${browserName} - ${currentBuild.fullDisplayName}",
                body: """
                ✅ Tests completed on ${browserName}.
                Headless mode: ${params.HEADLESS}
                Reports attached.
                """,
                to: params.EMAIL_TO,
                cc: params.EMAIL_CC,
                bcc: params.EMAIL_BCC,
                attachLog: true,
                attachmentsPattern: "test-reports-${browserName}.zip"
            )
        }
    }
}
