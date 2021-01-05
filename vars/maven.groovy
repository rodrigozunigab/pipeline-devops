def call(stageOptions){
        def buildEjecutado = false;
  
         stage("Validar"){

           if (
                stageOptions.contains('Compile Code')           ||
                stageOptions.contains('Test Code')              ||
                stageOptions.contains('Jar')                    ||
                stageOptions.contains('SonarQube analysis')     ||
                stageOptions.contains('Upload Nexus')           || 
                (stageOptions =='')
               ) {
               echo "Ok, se continua con los stage, ya que ingreso parametros conocidos"
            } else {
                currentBuild.result = 'FAILURE'
                echo "No se puede ejecutar este pipeline, ya que no ingreso parametros conocidos"
            }   

       }

        stage('Compile Code') {
                env.TAREA =  env.STAGE_NAME 
                buildEjecutado =false;
                if (stageOptions.contains('Compile Code') || (stageOptions ==''))  { 
                    sh 'mvn clean compile -e'                    
                }
        }

        stage('Test Code') {     
                env.TAREA =  env.STAGE_NAME  
                if ((stageOptions.contains('Test') || (stageOptions =='')) ) {      
                    sh 'mvn clean test -e'
                } 
        }
        stage('Jar') {        
                env.TAREA =  env.STAGE_NAME 
                if ((stageOptions.contains('Test') || (stageOptions ==''))) {      
                    sh 'mvn clean package -e' 
                    buildEjecutado =true;
                }
        }
        stage('SonarQube analysis') {
            env.TAREA =  env.STAGE_NAME 
            if (!buildEjecutado) {
                currentBuild.result = 'FAILURE'
                echo "No se puede ejecutar Sonar sin haber ejecutado un Build"
                buildEjecutado = false;
            }    

            def scannerHome = tool 'sonar-scanner';    
            withSonarQubeEnv('sonar-server') { 
                if ((stageOptions.contains('Sonar') || (stageOptions =='')) && (buildEjecutado) )
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'  
            }  
        }         

        stage('Upload Nexus') {  
            env.TAREA =  env.STAGE_NAME   
            if ((stageOptions.contains('Nexus') || (stageOptions =='')) && (buildEjecutado) )          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '2.0.1']]]                     
        }            

}

return this;