pipeline{
    agent any
    stages {
        stage('Stack creation'){
           steps {
                jobDsl targets: "jobs.groovy"
            }
        }
        stage('Build and upload') {
            steps {
                build job: 'MNT-CD-module9-build-job',
                parameters: [string(name: 'ARTIFACT_NAME', value: 'helloworld-$BUILD_NUMBER')]
            }
        }
    }
}