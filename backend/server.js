const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

app.get('/status', (request, response) => response.json({clients: clients.length}));

const PORT = 3000;

let clients = [];
let images = [];

function imagesHandler(request, response, next) {
    const headers = {
      'Content-Type': 'text/event-stream',
      'Connection': 'keep-alive',
      'Cache-Control': 'no-cache'
    };
    response.writeHead(200, headers);
  
    const data = `data: ${JSON.stringify(images)}\n\n`;
  
    response.write(data);
  
    const clientId = Date.now();
  
    const newClient = {
      id: clientId,
      response
    };
  
    clients.push(newClient);

    request.on('close', () => {
      console.log(`${clientId} Connection closed`);
      clients = clients.filter(client => client.id !== clientId);
    });
}
  
app.get('/images', imagesHandler);


function sendImagesToAll(newImage) {
    clients.forEach(client => client.response.write(`data: ${JSON.stringify(newImage)}\n\n`))
}
  
async function addImage(request, respsonse, next) {
    const newImage = request.body;
    images.push(newImage);
    respsonse.json(newImage)
    return sendImagesToAll(newImage);
}
  
app.post('/images', addImage);

app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`)
})