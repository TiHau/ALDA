import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class TUI {
    public static void main(String[] args) {
        Dictionary<String, String> dic = new SortedArrayDictionary();
        Scanner in = new Scanner(System.in);
        System.out.println("Started TUI. Type exit to quit. \n" +
                "New SortedArrayDictionary was created.");

        while (true) {
            String[] cline = in.nextLine().split(" ");
            switch (cline[0]) {
                case "create":
                    if (cline.length < 2) {
                        System.out.println("Keine Angabe der Dictionary-Art");
                        break;
                    }
                    switch (cline[1]) {
                        case "sad":
                            dic = new SortedArrayDictionary<>();
                            System.out.println("New SortedArrayDictionary was created.");
                            break;
                        case "hd":
                            dic = new HashDictionary<>();
                            System.out.println("New Hash-Dictionary was created.");
                            break;
                        case "btd":
                            dic = new BinaryTreeDictionary<>();
                            System.out.println("New BinaryTree-Dirctionary was created.");
                            break;
                        default:
                            System.out.println("unknow Dictionary");
                            break;
                    }
                    break;

                case "read":
                    if (cline.length < 2) {
                        System.out.println("Kein Pfad angegeben");
                        break;
                    }
                    double rt = System.nanoTime();
                    int n;
                    String p;
                    try {
                        n = Integer.parseInt(cline[1]);
                        p = cline[2];
                    } catch (NumberFormatException e) {
                        n = Integer.MAX_VALUE;
                        p = cline[1];
                    }
                    try (BufferedReader br = new BufferedReader(new FileReader(p))) {
                        String line = br.readLine();
                        for (int i = 0; i < n && line != null; i++) {
                            String[] tmp = line.split(" ");
                            dic.insert(tmp[0], tmp[1]);
                            line = br.readLine();
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("FileNotFound");
                    } catch (IOException e) {
                        System.err.println("IOException");
                    }
                    System.out.println((System.nanoTime()-rt)/1000/1000);
                    break;

                case "p":
                    for (Dictionary.Entry e : dic) {
                        System.out.println(e.getKey() + ": " + e.getValue());
                    }
                    break;
                case "s":
                    if (cline.length < 2) {
                        System.out.println("command: s \"Key\"");
                        break;
                    }
                    String sV = dic.search(cline[1]);
                    if (sV == null) {
                        System.out.println("Kein Eintrage für \"" + cline[1] + "\" gefunden. ._.");
                    } else {
                        System.out.println("Value: " + sV);
                    }
                    break;
                case "i":
                    if (cline.length < 3) {
                        System.out.println("command: i \"Key\" \"Value\"");
                        break;
                    }
                    if (dic.insert(cline[1], cline[2]) == null) {
                        System.out.println("Eintrag wurde hinzugefügt.");
                    } else {
                        System.out.println("Eintrag schon vorhanden. Werte wurden getauscht.");
                    }
                    break;
                case "r":
                    if (cline.length < 2) {
                        System.out.println("command: r \"Key\"");
                        break;
                    }
                    String rmV = dic.remove(cline[1]);
                    if (rmV == null) {
                        System.out.println("Key not found");
                    } else {
                        System.out.println("Eintrag wurde entfernt.");
                    }
                    break;
                case "exit":
                    System.out.println("See you next time :)");
                    return;

                case "lz":
                    LinkedList<String> l = new LinkedList<>();
                    for (Dictionary.Entry e : dic) {
                        l.add((String) e.getValue());
                    }
                    double t = System.nanoTime();
                    for (String s : l) {
                        dic.search(s);
                    }
                    t = (System.nanoTime() - t) / 1000 / 1000;
                    System.out.println(t);
                    break;
                default:
                    System.out.println("Ungültiger Befehl!");
            }
        }
    }
}
