package org.elasticsearch.plugin.analysis;

import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.analysis.CocCocTokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Collections;
import java.util.Map;

public class CocCocTokenizerPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        return Collections.singletonMap("coccoc_tokenizer", CocCocTokenizerFactory::new);
    }
}
