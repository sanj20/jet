// -*- tab-width: 4 -*-
//Title:        JET
//Version:      1.00
//Copyright:    Copyright (c) 2000
//Author:       Ralph Grishman
//Description:  A Java-based Information Extraction Tool

package edu.nyu.jet.lex;

import java.util.Vector;

import edu.nyu.jet.aceJet.Ace;
import edu.nyu.jet.lisp.FeatureSet;
import edu.nyu.jet.tipster.Annotation;
import edu.nyu.jet.tipster.Document;

/**
 *  the basic element of the (internal) lexicon:  the set of definitions of
 *  a word or sequence of words.
 */

public class LexicalEntry {

  String words[];
  Vector<FeatureSet> definitions;
  String type = "constit";

  /**
   *  creates a lexical entry for the sequence of words <I>wds</I> and
   *  makes <I>fs</I> one definition of this sequence.
   */

  public LexicalEntry(String wds[], FeatureSet fs) {
    words = wds;
    definitions = new Vector<FeatureSet>();
    definitions.addElement(fs);
  }

  public LexicalEntry(String wds[], FeatureSet fs, String type) {
    words = wds;
    definitions = new Vector<FeatureSet>();
    definitions.addElement(fs);
    this.type = type;
  }

  /**
   *  adds <I>fs</I> as an additional definition for this lexical entry
   */

  public void addDefinition (FeatureSet fs) {
    definitions.addElement(fs);
  }

  /**
   *  returns the definitions for the lexical entry
   */

  public FeatureSet[] getDefinition () {
    return definitions.toArray(new FeatureSet[0]);
  }

  /**
   *  determines whether the lexical entry matches the sequence of
   *  tokens <I>wds</I>.  Returns true if it does, otherwise false.
   */

  public boolean matches (String wds[]) {
    if (words.length == wds.length) {
      for (int i=0; i < words.length; i++) {
        if (! words[i].equals(wds[i])) return false;
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   *  determines whether the lexical entry matches the tokens in
   *  Document doc, starting at position posn.  If it does, it
   *  returns the end position of the last token matched;  otherwise
   *  it returns 0.  If a token is marked case=forcedCap, a
   *  case-independent comparison is done between the entry and the token;
   *  otherwise the comparison is case-sensitive.
   */

  public int matches (Document doc, int posn) {
    for (int i=0; i < words.length; i++) {
      Annotation ann = doc.tokenAt(posn);
      if (ann == null) return 0;
      String token = doc.text(ann).trim();
      boolean forcedCap = (ann.get("case") == "forcedCap") || Ace.monocase;
      if (forcedCap) {
        if (! token.equalsIgnoreCase(words[i])) return 0;
      } else {
        if (! token.equals(words[i])) return 0;
      }
      posn = ann.span().end();
      // while ((posn < doc.length()) && Character.isWhitespace(doc.charAt(posn))) posn++;
    }
    return posn;
  }

}
