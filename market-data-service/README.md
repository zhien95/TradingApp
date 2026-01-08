# Market Data Service - Event-Based Updates

This service provides real-time market data updates using Server-Sent Events (SSE) for frontend consumption.

## Features

- Real-time market data streaming via Server-Sent Events
- Support for individual symbol streams
- Support for all symbols stream
- REST API endpoints for traditional requests
- Kafka integration for event distribution

## Endpoints

### REST Endpoints

- `GET /api/market-data/price/{symbol}` - Get current price for a symbol
- `GET /api/market-data/historical/{symbol}?limit={n}` - Get historical data
- `GET /api/market-data/symbols` - Get all available symbols
- `GET /api/market-data/symbols/{symbol}` - Get specific symbol info
- `POST /api/market-data/symbols` - Add a new symbol
- `PUT /api/market-data/update` - Update market data
- `GET /api/market-data/exists/{symbol}` - Check if symbol exists

### SSE Streaming Endpoints

- `GET /api/market-data/stream/{symbol}` - Stream real-time updates for a specific symbol
- `GET /api/market-data/stream-all` - Stream real-time updates for all symbols

## Event-Based Market Data Streaming

### Server-Sent Events (SSE)

The service uses Server-Sent Events to push real-time market data updates to connected clients. SSE is ideal for:

- One-way communication from server to client
- Real-time data streaming
- Better than WebSockets for server-driven updates
- Lower complexity than WebSockets
- Automatic reconnection handling

### Event Format

All streaming endpoints emit events with the name `market-update` containing JSON market data:

```json
{
  "symbol": "AAPL",
  "price": 150.25,
  "bidPrice": 150.20,
  "askPrice": 150.30,
  "highPrice": 152.10,
  "lowPrice": 149.80,
  "openPrice": 149.90,
  "closePrice": 150.00,
  "volume": 1250000,
  "timestamp": "2026-01-07T10:30:00"
}
```

## Frontend Integration

### Using the Market Data Client

A JavaScript client library is provided at `/market-data-client.js`:

```html

<script src="/market-data-client.js"></script>
<script>
    // Initialize the client
    const marketClient = new MarketDataClient("http://localhost:8080/market-data-service");

    // Connect to updates for a specific symbol
    const streamId = marketClient.connectToSymbol('AAPL',
            (data) => {
                console.log('Received update:', data);
                // Update your UI with the new market data
                updatePriceDisplay(data);
            },
            (error) => {
                console.error('Connection error:', error);
            }
    );

    // To disconnect
    marketClient.disconnect(streamId);
</script>
```

### Direct SSE Usage

You can also use the native EventSource API directly:

```javascript
const eventSource = new EventSource('/api/market-data/stream/AAPL');

eventSource.addEventListener('market-update', (event) => {
    const data = JSON.parse(event.data);
    console.log('Market update:', data);
    // Update your UI
});

eventSource.onerror = (error) => {
    console.error('SSE connection error:', error);
};
```

### Demo Page

A demo page is available at `/market-data-stream.html` to test the streaming functionality.

## Implementation Details

### Server-Side Implementation

- Uses Spring's `SseEmitter` for Server-Sent Events
- Maintains active connections in thread-safe collections
- Automatically cleans up disconnected clients
- Integrates with existing Kafka event system
- Async processing for non-blocking event delivery

### Data Flow

1. Market data is updated via REST API or other services
2. Updated data is sent to Kafka for internal service communication
3. Updated data is broadcast to all active SSE connections
4. Connected frontend clients receive real-time updates

## Testing

To test the streaming functionality:

1. Start the market data service
2. Navigate to `/market-data-stream.html`
3. Connect to a symbol stream or all streams
4. Update market data through the REST API to see real-time updates

## Performance Considerations

- SSE connections are maintained in memory
- Automatic cleanup of disconnected clients
- Async processing prevents blocking the main thread
- Memory usage scales with active connections
- Recommended to implement connection limits in production