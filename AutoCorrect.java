import java.util.*;

/**
 * There is a broken keyboard in which space gets typed when you type 
 * the letter 'y'. 
 * 
 * Given an input string which is the output from the keyboard. 
 * A dictionary of possible words is also provided as an input param. 
 * 
 * Return a list of possible actual input typed by the user. 
 * 
 * Example Input: 
 *   String: "big bull ellen is a outuber" 
 *   Dictionary: [
 *     "big", "bull", "bully", "ellen", "yellen", 
 *     "is", "a", "ay", "youtuber", "outuber"
 *   ] 
 * Expected Output: 
 *   List: [
 *     "big bull yellen is a youtuber", 
 *     "big bull yellen is ay outuber",
 *     "big bully ellen is a youtuber", 
 *     "big bully ellen is ay outuber"
 *   ]
 */
class AutoCorrect {
    public static void main(String[] args) {
        String input = "big bull  ellen is a  outuber";
        // for (int i = 0; i < 3; i++) {
        //     input = input + " " + "big bull  ellen is a  outuber";
        // }
        System.out.println("input length " + input.length());
        HashSet<String> dict = new HashSet<String>(Arrays.asList(
                new String[] { "big", "bull", "bully", "ellen", "yellen", "is", "a", "ay", "youtuber", "outuber" }));

        // Use ArrayList<ArrayList>
        ArrayList<String> sentences = new ArrayList<String>();
        long timeC = System.currentTimeMillis();
        ArrayList<ArrayList<String>> results = guess(input, dict);
        results.forEach((s) -> {
            sentences.add(String.join(" ", s));
        });
        long timeD = System.currentTimeMillis();
        System.out.println("Elapsed time ArrayList: " + (timeD - timeC));
        // System.out.println(sentences);


        // Use TreeNode & backtrack
        ArrayList<String> sentences2 = new ArrayList<String>();
        long timeA = System.currentTimeMillis();
        ArrayList<TreeNode<String>> results2 = guess2(input, dict);
        results2.forEach((s) -> {
            sentences2.add(s.backtrack());
        });
        long timeB = System.currentTimeMillis();
        System.out.println("Elapsed time TreeNode: " + (timeB - timeA));
        // System.out.println(sentences2);
    }

    static ArrayList<ArrayList<String>> guess(String input, Set<String> dict) {
        return guessHelper(new ArrayList<String>(), "", dict, input);
    }

    static ArrayList<TreeNode<String>> guess2(String input, Set<String> dict) {
        ArrayList<TreeNode<String>> results = new ArrayList<TreeNode<String>>();
        guessHelper2(results, new TreeNode<String>(), "", dict, input);
        return results;
    }

    static void guessHelper2(ArrayList<TreeNode<String>> results, TreeNode<String> sentence, String part,
            Set<String> dict, String rest) {
        // System.out.println("============");
        // System.out.println(sentence);
        // System.out.println(part);
        // System.out.println(dict);
        // System.out.println(rest);
        // System.out.println("============");

        if (rest.length() == 0) {
            if (dict.contains(part)) {
                TreeNode<String> ns = new TreeNode<String>(part, sentence);
                results.add(ns);
            }
            return;
        }

        char next = rest.charAt(0);

        if (next == ' ') {
            if (dict.contains(part)) {
                TreeNode<String> ns = new TreeNode(part, sentence);
                guessHelper2(results, ns, "", dict, rest.substring(1));
            }
            guessHelper2(results, sentence, part + "y", dict, rest.substring(1));
        } else {
            guessHelper2(results, sentence, part + next, dict, rest.substring(1));
        }
    }

    static ArrayList<ArrayList<String>> guessHelper(ArrayList<String> sentence, String part, Set<String> dict,
            String rest) {
        // System.out.println("============");
        // System.out.println(sentence);
        // System.out.println(part);
        // System.out.println(dict);
        // System.out.println(rest);
        // System.out.println("============");

        ArrayList<ArrayList<String>> sentences2 = new ArrayList<ArrayList<String>>();
        if (rest.length() == 0) {
            if (dict.contains(part)) {
                ArrayList<String> ns = cloneList(sentence);
                ns.add(part);
                sentences2.add(ns);
            }
            return sentences2;
        }
        char next = rest.charAt(0);

        if (next == ' ') {
            if (dict.contains(part)) {
                ArrayList<String> ns = cloneList(sentence);
                ns.add(part);
                sentences2.addAll(guessHelper(ns, "", dict, rest.substring(1)));
            }
            ArrayList<String> nse = cloneList(sentence);
            sentences2.addAll(guessHelper(nse, part + "y", dict, rest.substring(1)));
        } else {
            ArrayList<String> ns = cloneList(sentence);
            sentences2.addAll(guessHelper(ns, part + next, dict, rest.substring(1)));
        }

        return sentences2;
    }

    static ArrayList<String> cloneList(ArrayList<String> list) {
        return (ArrayList<String>) list.clone();
    }

}

class TreeNode<T> {
    T value;
    TreeNode<T> parent;

    public TreeNode(T value, TreeNode<T> parent) {
        this.value = value;
        this.parent = parent;
    }

    public TreeNode() {
        this.value = null;
        this.parent = null;
    }

    public TreeNode<T> clone() {
        return new TreeNode<T>(this.value, this.parent);
    }

    public String backtrack() {
        if (this.value == null) {
            return "";
        }
        if (this.parent == null) {
            return this.value.toString();
        }
        return this.parent.backtrack().trim() + " " + this.value.toString();
    }
}