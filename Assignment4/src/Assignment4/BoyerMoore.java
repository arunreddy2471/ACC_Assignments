
package Assignment4;

import java.util.ArrayList;
import java.util.List;

/***************************************************************
 *  Compilation:  javac BoyerMoore.java
 *  Execution:    java BoyerMoore pattern text
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  bad-character rule part of the Boyer-Moore algorithm.
 *  (does not implement the strong good suffix rule)
 *
 *  % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:               abracadabra
 *
 *  % java BoyerMoore rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:         rab
 *
 *  % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:                                   bcara
 *
 *  % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ***************************************************************/

public class BoyerMoore {
    private final int R;     // the radix
    private int[] right;     // the bad-character skip array

    private char[] pattern;  // store the pattern as a character array
    private String pat;      // or as a string

    // pattern provided as a string
    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        // position of rightmost occurrence of c in the pattern
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pat.length(); j++)
            right[pat.charAt(j)] = j;
    }

    // pattern provided as a character array
    public BoyerMoore(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // position of rightmost occurrence of c in the pattern
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }

    // return offset of first match; N if no match
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i+j)) {
                    skip = Math.max(1, j - right[txt.charAt(i+j)]);
                    break;
                }
            }
            if (skip == 0) return i;    // found
        }
        return N;                       // not found
    }


    // return offset of first match; N if no match
    public int search(char[] text) {
        int M = pattern.length;
        int N = text.length;
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M-1; j >= 0; j--) {
                if (pattern[j] != text[i+j]) {
                    skip = Math.max(1, j - right[text[i+j]]);
                    break;
                }
            }
            if (skip == 0) return i;    // found
        }
        return N;                       // not found
    }
	static int NO_OF_CHARS = 256;

	static int max(int a, int b) {
		return (a > b) ? a : b;
	}
	static void badCharHeuristic(char[] str, int size, int badchar[]) {
		int i;
		for (i = 0; i < NO_OF_CHARS; i++)
			badchar[i] = -1;
		for (i = 0; i < size; i++)
			badchar[(int) str[i]] = i;
	}
	static List<Integer> BoyerMooreSearch(char txt[], char pat[]) {
		int m = pat.length;
		int n = txt.length;
		List<Integer> matchedIndexes = new ArrayList<>();
		int badchar[] = new int[NO_OF_CHARS];
		badCharHeuristic(pat, m, badchar);

		int s = 0;
		while (s <= (n - m)) {
			int j = m - 1;

			while (j >= 0 && pat[j] == txt[s + j])
				j--;

			if (j < 0) {
				// System.out.println("Patterns occur at shift = " + s);
				matchedIndexes.add(s);
				s += (s + m < n) ? m - badchar[txt[s + m]] : 1;
			}

			else
				s += max(1, j - badchar[txt[s + j]]);
		}
		if (matchedIndexes.size() == 0) {
			matchedIndexes.add(n);
		}
		return matchedIndexes;
	}


    // test client
    public static void main(String[] args) {
        //String pat = args[0];
        //String txt = args[1];
 	   
        // There are two implmentations of search
 	   // one is with String and the other is an array of chars
 	   
        String pat = "abracadabra";
        String txt = "abacadabrabracabracadabrabrabrac-fdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdslfdskjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgkjgad";
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        BoyerMoore boyermoore1 = new BoyerMoore(pat);
        BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat + " at pos " + offset1);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat + " at pos " + offset2);
      }
}