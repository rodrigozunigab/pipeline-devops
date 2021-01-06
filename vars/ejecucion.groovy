def call(){
pipeline {
    agent any
    parameters { 
        choice(name: 'HERRAMIENTA', choices: ['gradle', 'maven'], description: 'opcion de compilacion')
        string(name: 'stage' , defaultValue: '', description: '')
    }
    
    stages {
        stage('Pipeline') {
            steps {
                script {
                //segun el valor del parametro se debe llamar a gradle o maven
                sh 'env'
                fliglet 'HOLA RZB'
                env.TAREA = ''
                echo "1.-HERRAMIENTA SELECCIONADA: ${params.HERRAMIENTA}" 
                echo "2.-PARAMETROS SELECCIONADOS: ${stage}"   
                echo "3.-RUNNING ${env.BUILD_ID} on ${env.JENKINS_URL}"   

                                          
                if (params.HERRAMIENTA == 'gradle'){
                        gradle.call(stage);
                } else {  
                        maven.call(stage);                 
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