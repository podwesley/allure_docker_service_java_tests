FROM openjdk:8-jdk

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

# Install Chrome and dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    libglib2.0-0 \
    libnss3 \
    libgconf-2-4 \
    libfontconfig1 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    libgbm1 \
    libxshmfence1

# Install Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable

# Set up working directory
WORKDIR /app

# Copy project files
COPY src .

# Set Chrome binary path for Selenium
ENV CHROME_BIN=/usr/bin/google-chrome

# Set environment variable to indicate Docker environment
ENV DOCKER_CONTAINER=true

# Create directories for Allure reports
RUN mkdir -p allure-results allure-reports

# Set display for headless Chrome
ENV DISPLAY=:99

# Run tests and generate Allure report
CMD ["sh", "-c", "mvn test -Dtest=CucumberRunner && echo 'Testes concluídos. Resultados do Allure disponíveis no diretório allure-results.'"]
