/**
 * Market Data Streaming Client
 * Provides an easy-to-use interface for consuming real-time market data via Server-Sent Events
 */

class MarketDataClient {
    constructor(baseUrl = '') {
        this.baseUrl = baseUrl;
        this.eventSources = new Map(); // Store active connections by stream ID
        this.listeners = new Map(); // Store event listeners
    }

    /**
     * Connect to real-time updates for a specific symbol
     * @param {string} symbol - The trading symbol (e.g., 'AAPL')
     * @param {function} onUpdate - Callback function called when updates are received
     * @param {function} onError - Optional error callback
     * @returns {string} Stream ID that can be used to disconnect
     */
    connectToSymbol(symbol, onUpdate, onError = null) {
        const streamId = `symbol-${symbol}`;

        // Close existing connection if it exists
        if (this.eventSources.has(streamId)) {
            this.disconnect(streamId);
        }

        const url = `${this.baseUrl}/api/market-data/stream/${symbol}`;
        const eventSource = new EventSource(url);

        eventSource.onopen = () => {
            console.log(`Connected to market data stream for symbol: ${symbol}`);
        };

        eventSource.addEventListener('market-update', (event) => {
            try {
                const data = JSON.parse(event.data);
                onUpdate(data);
            } catch (error) {
                console.error('Error parsing market data:', error);
            }
        });

        eventSource.onerror = (error) => {
            console.error(`Error in market data stream for symbol ${symbol}:`, error);
            if (onError) {
                onError(error);
            }
        };

        this.eventSources.set(streamId, eventSource);
        return streamId;
    }

    /**
     * Connect to real-time updates for all symbols
     * @param {function} onUpdate - Callback function called when updates are received
     * @param {function} onError - Optional error callback
     * @returns {string} Stream ID that can be used to disconnect
     */
    connectToAll(onUpdate, onError = null) {
        const streamId = 'all';

        // Close existing connection if it exists
        if (this.eventSources.has(streamId)) {
            this.disconnect(streamId);
        }

        const url = `${this.baseUrl}/api/market-data/stream-all`;
        const eventSource = new EventSource(url);

        eventSource.onopen = () => {
            console.log('Connected to market data stream for all symbols');
        };

        eventSource.addEventListener('market-update', (event) => {
            try {
                const data = JSON.parse(event.data);
                onUpdate(data);
            } catch (error) {
                console.error('Error parsing market data:', error);
            }
        });

        eventSource.onerror = (error) => {
            console.error('Error in market data stream for all symbols:', error);
            if (onError) {
                onError(error);
            }
        };

        this.eventSources.set(streamId, eventSource);
        return streamId;
    }

    /**
     * Disconnect from a specific stream
     * @param {string} streamId - The stream ID returned by connect methods
     */
    disconnect(streamId) {
        if (this.eventSources.has(streamId)) {
            const eventSource = this.eventSources.get(streamId);
            eventSource.close();
            this.eventSources.delete(streamId);
            console.log(`Disconnected from stream: ${streamId}`);
        }
    }

    /**
     * Disconnect from all active streams
     */
    disconnectAll() {
        for (const [streamId, eventSource] of this.eventSources) {
            eventSource.close();
            console.log(`Disconnected from stream: ${streamId}`);
        }
        this.eventSources.clear();
    }

    /**
     * Check if a stream is active
     * @param {string} streamId - The stream ID to check
     * @returns {boolean} True if the stream is active
     */
    isActive(streamId) {
        if (!this.eventSources.has(streamId)) {
            return false;
        }
        const eventSource = this.eventSources.get(streamId);
        return eventSource.readyState === EventSource.OPEN;
    }

    /**
     * Get the number of active connections
     * @returns {number} Number of active connections
     */
    getActiveConnectionCount() {
        return this.eventSources.size;
    }
}

// Example usage:
/*
// Initialize the client
const marketClient = new MarketDataClient();

// Connect to updates for a specific symbol
const symbolStreamId = marketClient.connectToSymbol('AAPL', 
    (data) => {
        console.log('Received update for AAPL:', data);
        // Update your UI with the new market data
        updatePriceDisplay(data);
    },
    (error) => {
        console.error('Connection error:', error);
    }
);

// Connect to updates for all symbols
const allStreamId = marketClient.connectToAll(
    (data) => {
        console.log('Received update for:', data.symbol, data);
        // Handle updates for any symbol
    }
);

// Disconnect when done
marketClient.disconnect(symbolStreamId);
marketClient.disconnectAll();
*/