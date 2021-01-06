def call(){
pipeline {
    agent any
    
    stages {
        stage('Pipeline') {
            steps {
                script {
                //segun el valor del parametro se debe llamar a gradle o maven
                sh 'env'
                fliglet 'HOLA RZB'
                env.TAREA = '' 
                echo "1.-RUNNING ${env.BUILD_ID} on ${env.JENKINS_URL}" 
                echo "2.-GIT_BRANCH ${env.GIT_BRANCH}"   

                                          
                if ((${env.GIT_BRANCH} == 'develop') || (${env.GIT_BRANCH} == 'feature')){
                        gradleci.call(stage);
                } else if (${env.GIT_BRANCH} == 'release'){  
                        gradlecd.call(stage);                 
                }

                }
            }
        }
    }

    post {
        success{
            //: [Nombre Alumno][Nombre Job][buildTool] Ejecución exitosa
            slackSend color: 'good', message: "[Rodrigo Zuniga][${env.JOB_NAME}][${env.HERRAMIENTA}]Ejecucion exitosa"           
        }

        failure{
            //[Nombre Alumno][Nombre Job][buildTool] Ejecución fallida en stage [Stage]
            //la variable env.TAREA esta definida en los groovy
            slackSend color: 'danger', message: "[Rodrigo Zuniga][${env.JOB_NAME}][${env.HERRAMIENTA}]Ejecución fallida en stage [${env.TAREA}]"                   
        }
    }

}


}

return this;