#!/bin/bash

# Navigates to the directory containing compiled Java files
cd ../java

# Runs the Java classes
java httpServer.Server &
java views.client.MainClientUI &
java views.admin.MainServerUI &

echo "All services started"
