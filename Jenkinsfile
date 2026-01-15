pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    parameters {
        choice(
            name: 'HEADLESS',
            choices: ['true', 'false'],
            description: 'Run in headless mode?'
        )
        extendedChoice(
            name: 'BROWSERS',
            type: 'PT_CHECKBOX',
            value: 'chrome,firefox,edge,safari',
            description: 'Select one or more browsers to run tests on',
            multiSelectDelimiter: ','
        )
        string(
            name: 'EMAIL_TO',
            defaultValue: 'qa-team@example.com',
            description: 'Comma-separated list of primary recipients'
        )
        string(
            name: 'EMAIL_CC',
            defaultValue: '',
            description: 'Comma-separated list of CC recipients'
        )
        string(
            name: 'EMAIL_BCC',
            defaultValue: '',
            description: 'Comma-separated list of BCC recipients'
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
                    // Split selected browsers into a list
                    def browsers = params.BROWSERS.split(',')

                    // Prepare parallel branches
                    def branches = [:]

                    for (b in browsers) {
                        def browserName = b.trim()
                        branches[browserName] = {
                            runTestsForBrowser(browserName)
                        }
                    }

                    parallel branches
                }
            }
        }
    }
}

def runTestsForBrowser(String browser) {
    stage("Tests on ${browser}") {
        withEnv(["BROWSER=${browser}", "HEADLESS=${params.HEADLESS}"]) {
            // Run Maven tests with TestNG
            bat "mvn clean test -DsuiteXmlFile=testng.xml -Dbrowser=${browser} -Dheadless=${params.HEADLESS}"

            // Archive artifacts
            archiveArtifacts artifacts: "test-reports/**", fingerprint: true

            // Create ZIP file for email
            bat "powershell Compress-Archive -Path test-reports/* -DestinationPath test-reports-${browser}.zip"

            // Send email
            emailext(
                subject: "Selenium Tests on ${browser} - ${currentBuild.fullDisplayName}",
                body: """
                ✅ Tests completed on ${browser}.
                Headless mode: ${params.HEADLESS}
                Reports attached.
                """,
                to: params.EMAIL_TO,
                cc: params.EMAIL_CC,
                bcc: params.EMAIL_BCC,
                attachLog: true,
                attachmentsPattern: "test-reports-${browser}.zip"
            )
        }
    }
}
