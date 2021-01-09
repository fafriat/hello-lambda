# Let's compile your Aws Lambda in Java

This repository include code explained in my blog https://frank-afriat.medium.com/lets-compile-your-aws-lambda-in-java-eafc24aae269

## hello-lambda-java11: simple java 11, maven lambda code

build it with ```mvn package```

## hello-lambda-custom: for building a custom runtime

build it with ```docker build -t hello-lambda/latest .```


## hello-lambda-custom-witth-logs: for building a custom runtime with slf4j-simple-lambda

Explanations from my blog: https://frank-afriat.medium.com/solving-the-java-aws-lambda-logging-problem-305b06df457f

Build: 

Generate the Dockerfile in target folder:  
```mvn validate```

Then build it with 
```docker build -f target/Dockerfile -t hello-lambda:latest .```

Then extract the function.zip from the image:
```docker cp $(docker create hello-lambda:latest):/function.zip .```
