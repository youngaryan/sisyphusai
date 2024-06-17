package main.java.com.sisyphusai.lichess;

import java.util.stream.Stream;

import chariot.ClientAuth;
import chariot.model.Event;
import chariot.model.GameStateEvent;
import chariot.model.Many;
import chariot.model.One;

public class LichessClient {
    private final ClientAuth client;

    public LichessClient(String authToken) {
        this.client = ClientAuth.auth(authToken);
    }

    public Many<Event> streamEvents() {
        return client.bot().connect();
    }

    public Stream<GameStateEvent> streamGameEvents(String gameId) {
        return client.bot().connectToGame(gameId).stream();
    }

    public One<Void> move(String gameId, String moveUci) {
        return client.bot().move(gameId, moveUci);
    }

    public One<Void> move(String gameId, String moveUci, boolean drawOffer) {
        return client.bot().move(gameId, moveUci, drawOffer);
    }

    public One<Void> abort(String gameId) {
        return client.bot().abort(gameId);
    }

    public One<Void> resign(String gameId) {
        return client.bot().resign(gameId);
    }

    // ADD CHAT LOGIC LATER
}
