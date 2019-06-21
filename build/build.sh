#!/bin/sh

echo "Updating coccoc-tokenizer submodule..."
git submodule update --init
echo "Replace some coccoc-tokenizer files with custom coccoc-tokenizer files..."
cp -f coccoc-tokenizer-custom/build_java.sh coccoc-tokenizer/java
cp -f coccoc-tokenizer-custom/Tokenizer.java coccoc-tokenizer/java/src/java
echo "Building..."
if [ -d "tokenizer" ]; then
  rm -rf tokenizer
fi
mkdir tokenizer
BUILD_DIRECTORY=$PWD
INSTALL_DIRECTORY=$BUILD_DIRECTORY/tokenizer
echo "Installing coccoc-tokenizer to folder ${INSTALL_DIRECTORY}..."
cd coccoc-tokenizer
if [ -d "build" ]; then
  rm -rf build
fi
mkdir build && cd build
cmake -DBUILD_JAVA=1 -DCMAKE_INSTALL_PREFIX=${INSTALL_DIRECTORY} .. && make install
echo "Install coccoc-tokenizer done!"
echo "Removing redundant files..."
cd ..
rm -rf build
echo "Building coccoc-tokenizer plugin..."
cd $BUILD_DIRECTORY/..
# Add coccoc-tokenizer.jar to local repository
mvn install:install-file -Dfile=$BUILD_DIRECTORY/tokenizer/share/java/coccoc-tokenizer.jar -DgroupId=com.coccoc -DartifactId=coccoc-tokenizer -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true
# Build plugin
mvn clean install -U -DskipTests
cd $BUILD_DIRECTORY
