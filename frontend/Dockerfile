ARG NODE_VERSION=22.14.0

FROM node:${NODE_VERSION}-alpine AS base
WORKDIR /app
COPY package.json /app
RUN npm install

COPY . /app
EXPOSE 5173
CMD [ "npm", "run", "dev" ]
