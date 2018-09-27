#!/bin/bash
declare -a provider=("eclipselink" "hibernate-entitymanager")
declare -a database=("h2" "derby" "hsqldb")
for persistence in "${provider[@]}"
do
    for database in "${database[@]}"
    do
        echo =======================================================================================================
        echo =======================================================================================================
        echo $persistence/$database
        echo =======================================================================================================
        echo =======================================================================================================
        mvn -P$persistence,$database clean install
    done
done
