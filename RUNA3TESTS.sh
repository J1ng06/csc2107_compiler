#!/bin/bash

STARTDIR="$(pwd)"
cd "$(dirname "$0")"
BASEDIR="$(pwd)"
echo
echo "********************* ALL A3 Tests *********************"
cd "${STARTDIR}/tests/A3tests"
for f in *.488; do
      echo "^^^^^Processing $f^^^^^"
          java -jar "${BASEDIR}/dist/compiler488.jar" "$f";
              echo
            done
