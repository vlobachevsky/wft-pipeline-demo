#!/usr/bin/env groovy

import static groovy.io.FileType.FILES
import com.cloudbees.groovy.cps.NonCPS


properties([
  parameters([
    booleanParam(name: 'DEPLOY_ON_AP01', defaultValue: true,
        description: 'Deploys selected build on AP01 node.'),
    booleanParam(name: 'DEPLOY_ON_MW01', defaultValue: true,
        description: 'Deploys selected build on MW01 node.'),
    booleanParam(name: 'DEPLOY_ON_MW02', defaultValue: true,
        description: 'Deploys selected build on MW02 node.'),
    // choice(name: 'BUILD_TO_DEPLOY', defaultValue: '', choices: "$fileList",
    //     description: 'Logical group of agent to run the job on. ')
   ])
])


node('master') {

    stage('Select Package') {
        def userInput = input(
            id: 'userInput', message: 'Package to deploy:', parameters: [
                [
                    $class: 'ChoiceParameterDefinition',
                    name: 'PACKAGE_TO_DEPLOY',
                    choices: getAllFiles(),
                    description: '',
                ],
            ]
        )
        echo ("Selected Package :: "+userInput)
    }

    stage('Deploy AP01') {
        // echo "PACKAGE_TO_DEPLOY: $PACKAGE_TO_DEPLOY"
        echo "env.PACKAGE_TO_DEPLOY: $env.PACKAGE_TO_DEPLOY"
        echo "params.PACKAGE_TO_DEPLOY: $params.PACKAGE_TO_DEPLOY"

    }

}

@NonCPS
def getAllFiles() {
    def result = ''
    new File('D:\\Temp\\wfr-artifactory').traverse(type: FILES, nameFilter: ~/.*.zip/) { file ->
        result += "${file.name}\n"
    }
    return result
}

// def findAMIs() {
//     return UUID.randomUUID().toString().split('-').join('\n')
// }
