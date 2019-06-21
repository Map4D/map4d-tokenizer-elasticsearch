package org.apache.lucene.analysis;

import com.coccoc.Token;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;

public class CocCocTokenizer extends Tokenizer {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final boolean forTransforming;

    private final int tokenizeOption;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

    private com.coccoc.Tokenizer tokenizer;

    private int offset;

    private int skippedPositions;

    private Iterator<Token> tokens;

    public CocCocTokenizer(final boolean forTransforming, final int tokenizeOption) {
        super();
        this.forTransforming = forTransforming;
        this.tokenizeOption = tokenizeOption;
        this.offset = 0;
        this.skippedPositions = 0;
        this.tokenizer = AccessController.
                doPrivileged((PrivilegedAction<com.coccoc.Tokenizer>) com.coccoc.Tokenizer::getInstance);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
        while (tokens.hasNext()) {
            final Token token = tokens.next();
            final String tokenText = token.getText();
            if (accept(tokenText)) {
                posIncrAtt.setPositionIncrement(skippedPositions + 1);
                if (token.getType() ==  Token.Type.NUMBER) {
                    typeAtt.setType("number");
                }
                else {
                    typeAtt.setType("word");
                }
                final int length = tokenText.length();
                termAtt.copyBuffer(tokenText.toCharArray(), 0, length);
                offsetAtt.setOffset(correctOffset(offset), offset = correctOffset(offset + length));
                offset++;
                return true;
            }
            ++skippedPositions;
        }
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        offset = 0;
        skippedPositions = 0;
        tokenize(input);
    }

    @Override
    public void end() throws IOException {
        super.end();
        final int finalOffset = correctOffset(offset);
        offsetAtt.setOffset(finalOffset, finalOffset);
        posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
    }

    private boolean accept(final String token) {
        if (token.length() == 1) {
            return Character.isLetterOrDigit(token.charAt(0));
        }
        return true;
    }

    private void tokenize(final Reader input) throws IOException {
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        final StringBuilder sentence = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = input.read(buffer, 0, buffer.length)) != -1) {
            sentence.append(buffer, 0, numCharsRead);
        }
        tokens = AccessController.
                doPrivileged((PrivilegedAction<Iterator<Token>>) () ->
                        tokenizer.segment(sentence.toString(), forTransforming, tokenizeOption).iterator());
    }
}
