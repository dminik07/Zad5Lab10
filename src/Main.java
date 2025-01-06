import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first three digits of your bank account: ");
        String userPrefix = scanner.nextLine().trim();

        String nbpUrl = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";
        boolean foundAny = false;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new URL(nbpUrl).openStream()))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                // Ensure the line has at least a bank code
                if (parts.length < 1) continue;

                // The first column is the bank number (e.g., 8 digits)
                String bankNumber = parts[0];

                // If bankNumber starts with the user's prefix, show it once and stop
                if (bankNumber.startsWith(userPrefix)) {
                    // Combine the remaining parts to form the bank name
                    String bankName = (parts.length > 1)
                            ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length))
                            : "Unknown Bank Name";

                    System.out.println("Short Bank Number: " + bankNumber);
                    System.out.println("Bank Name: " + bankName);

                    foundAny = true;
                    break; // Stop after the first match
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading from NBP: " + e.getMessage());
        }

        if (!foundAny) {
            System.out.println("No bank information found for prefix: " + userPrefix);
        }
    }
}
