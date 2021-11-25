package controller;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

public
class TokenHandler {
   public static Supplier <String> tokenSupplier;

    static {
        tokenSupplier = () -> {

            StringBuilder token = new StringBuilder();
            long currentTimeInMilisecond = Instant.now().toEpochMilli();
            token.append(currentTimeInMilisecond);
            token.append("-");
            token.append(UUID.randomUUID().toString());
            String string = token.toString();
            return string;
        };
    }

}
