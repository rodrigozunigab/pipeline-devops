def call(){
pipeline {
    agent any
    
    stages {
        stage('Pipeline') {
            steps {
                script {
                //segun el valor del parametro se debe llamar a gradle o maven
                sh 'env'
                env.TAREA = '' 
                echo "-RUNNING ${env.BUILD_ID} on ${env.JENKINS_URL}" 
                echo "-GIT_BRANCH ${env.GIT_BRANCH}"   

                                          
                if (env.GIT_BRANCH == "develop" || env.GIT_BRANCH == "feature"){
                        gradleci.call();
                } else if (env.GIT_BRANCH.contains("release")){  
                        gradlecd.call();                 
                } else {
                    echo " La rama <${env.GIT_BRANCH}> no se proceso" 
                }

                }
            }
        }
    }



    post {
        success{
            //: [Nombre Alumno][Nombre Job][buildTool] Ejecución exitosa
            slackSend color: 'good', message: "[Rodrigo Zuniga][${env.JOB_NAME}][${env.GIT_BRANCH}]Ejecucion exitosa"           
        }

        failure{
            //[Nombre Alumno][Nombre Job][buildTool] Ejecución fallida en stage [Stage]
            //la variable env.TAREA esta definida en los groovy
            slackSend color: 'danger', message: "[Rodrigo Zuniga][${env.JOB_NAME}][${env.GIT_BRANCH}]Ejecución fallida en stage [${env.TAREA}]"                   
        }
    }

}


}

return this;