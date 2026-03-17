#!/bin/bash
# Script để build và deploy lên AWS Elastic Beanstalk

echo "=== Building Thesis Management Application ==="

# Build WAR file (sử dụng Ant)
echo "Building WAR file..."
ant clean dist

if [ $? -ne 0 ]; then
    echo "Build failed! Please check errors above."
    exit 1
fi

echo "Build successful! WAR file created at: dist/ThesisManagement.war"

# Check if EB CLI is installed
if command -v eb &> /dev/null; then
    echo ""
    echo "=== Deploying to Elastic Beanstalk ==="
    read -p "Do you want to deploy now? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        eb deploy
    else
        echo "Deployment cancelled. You can deploy later using: eb deploy"
    fi
else
    echo ""
    echo "EB CLI not found. Please deploy manually:"
    echo "1. Go to AWS Console -> Elastic Beanstalk"
    echo "2. Upload dist/ThesisManagement.war"
    echo ""
    echo "Or install EB CLI: pip install awsebcli"
fi

