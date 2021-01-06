def call(){
  
        def buildEjecutado = false;
        figlet 'Gradle'
        figlet 'Integracion Continua'

        stage("buildAndTest"){   
            env.TAREA =  env.STAGE_NAME 
            buildEjecutado =false;
 
            sh "./gradlew clean build -x test" 
            buildEjecutado =true;

            if (buildEjecutado) {        
                sh "./gradlew clean build"  
            }            
        }
        
        stage("sonar"){
            env.TAREA =  env.STAGE_NAME 
            if (!buildEjecutado) {
                currentBuild.result = 'FAILURE'
                echo "No se puede ejecutar Sonar sin haber ejecutado un Build"
                buildEjecutado = false;
            }    

            def scannerHome = tool 'sonar-scanner';    
            withSonarQubeEnv('sonar-server') { 
                if (buildEjecutado)
                    sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"   
            }                        
        }

        stage("runJar"){
            env.TAREA =  env.STAGE_NAME 
            if (buildEjecutado){ 
                sh "nohup bash gradlew bootRun &"
                sleep 20                        
            }
        }
        stage("rest"){
            env.TAREA =  env.STAGE_NAME 
            if (buildEjecutado) 
                sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
        }  

        stage("nexusCI"){    
            env.TAREA =  env.STAGE_NAME   
            if (buildEjecutado)          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '2.0.1']]]                     
        }                    

}

return this;