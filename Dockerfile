FROM node:18-alpine
WORKDIR /app
RUN npm install -g @anthropic-ai/claude-code
CMD ["claude"]
