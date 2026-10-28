package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long deposits = 0, withdrawals = 0;
        StringBuilder report = new StringBuilder("========== END-OF-DAY REPORT ==========\n");
        if (Files.exists(logsFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder, Files::isRegularFile)) {
                for (Path file : stream) {
                    if (reportFile.toAbsolutePath().normalize().equals(file.toAbsolutePath().normalize()))
                        continue;
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length >= 2) {
                                try {
                                    long amount = Long.parseLong(parts[parts.length - 1]);
                                    if (line.toUpperCase().contains("DEPOSIT"))
                                        deposits = Math.addExact(deposits, amount);
                                    if (line.toUpperCase().contains("WITHDRAW"))
                                        withdrawals = Math.addExact(withdrawals, amount);
                                } catch (NumberFormatException | ArithmeticException ignored) {
                                    /* Ignore malformed log lines. */ }
                            }
                        }
                    }
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    report.append(file.getFileName()).append(" | size=").append(attrs.size())
                            .append(" bytes | modified=").append(attrs.lastModifiedTime()).append('\n');
                }
            }
        }
        report.append("Total deposits    : ").append(deposits).append('\n')
                .append("Total withdrawals : ").append(withdrawals).append('\n')
                .append("Net flow          : ").append(deposits - withdrawals).append('\n')
                .append("=======================================");
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null)
            Files.createDirectories(parent);
        Files.writeString(reportFile, report.toString() + System.lineSeparator(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return report.toString();
    }
}
