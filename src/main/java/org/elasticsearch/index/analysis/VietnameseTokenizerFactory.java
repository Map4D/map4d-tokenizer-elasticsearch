package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.VietnameseTokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

public class VietnameseTokenizerFactory extends AbstractTokenizerFactory {

    private final boolean forTransforming;

    private final int tokenizeOption;

    public VietnameseTokenizerFactory(final IndexSettings indexSettings, final Environment environment,
                                      final String ignored, final Settings settings) {
        super(indexSettings, ignored, settings);
        forTransforming = settings.getAsBoolean("for_transforming", Boolean.FALSE);
        tokenizeOption = settings.getAsInt("tokenize_option", 0);
    }

    @Override
    public Tokenizer create() {
        return new VietnameseTokenizer(forTransforming, tokenizeOption);
    }
}
