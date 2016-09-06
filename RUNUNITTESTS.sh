#!/bin/bash

STARTDIR="$(pwd)"
cd "$(dirname "$0")"
BASEDIR="$(pwd)"

if [ ! -d "${STARTDIR}/lib" ];
then
    echo "    Run 'ant gettools' and 'ant dist' before running tests."
elif [ ! -d "${STARTDIR}/dist" ];
then
    echo "    Run 'ant dist' before running tests."
else
    ant runtests
    cat "${STARTDIR}/testOutput.txt"
    echo
    echo "Results logged in file ${STARTDIR}/testOutput.txt"
    echo
fi