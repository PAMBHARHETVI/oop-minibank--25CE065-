public class CommandParser {
    public static MiniBank.Command parse(String line) {

        String[] parts = line.trim().split("\\s+");

        if (parts.length != 3) {

            throw new IllegalArgumentException(
                    "Invalid command format"
            );
        }

        MiniBank.TransactionType type =
                MiniBank.TransactionType.valueOf(
                        parts[0].toUpperCase()
                );

        String accountNumber = parts[1];

        long amount = Long.parseLong(parts[2]);

        return new MiniBank.Command(
                type,
                accountNumber,
                amount
        );
    }
}
