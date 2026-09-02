import java.io.*;

class Result {

    /*
     * Complete the 'marsExploration' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts STRING s as parameter.
     */

    public static int marsExploration(String s) {
        int changes = 0;

        // Expected SOS pattern repeats every 3 characters.
        char[] pattern = {'S', 'O', 'S'};

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != pattern[i % 3]) {
                changes++;
            }
        }

        return changes;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));

        BufferedWriter bufferedWriter =
                new BufferedWriter(
                        new FileWriter(System.getenv("OUTPUT_PATH"))
                );

        String s = bufferedReader.readLine();

        int result = Result.marsExploration(s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
