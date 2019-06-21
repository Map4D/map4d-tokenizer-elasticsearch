# Vietnamese tokenizer plugin for elasticsearch
Vietnamese tokenizer plugin integrates Vietnamese language analysis into Elasticsearch. This project is using vietnamese tokenizer from coccoc https://github.com/coccoc/coccoc-tokenizer.
This plugin provides the `coccoc_analyzer` tokenizer with 2 configurations: 
```
for_transforming: this option is Cốc Cốc specific kept for backwards compatibility (true/ false)
tokenize_option: TOKENIZE_NORMAL = 0, TOKENIZE_HOST = 1 or TOKENIZE_URL = 2, just use TOKENIZE_NORMAL (0) if unsure
```

## Build plugin
Step 1: Install maven, cmake, make

Step 2: Go to folder **build** and run script **build.sh**

Step 3: When build done, the plugin (coccoc-tokenizer-plugin-6.5.3.zip) will be in folder **target/releases**

Currently, We don't support build plugin on Window.
## Install plugin

```
bin/elasticsearch-plugin install file:///${project_dir}/target/releases/coccoc-tokenizer-plugin-6.5.3.zip
```
## Thank to
- Cốc Cốc for their coccoc-tokenizer opensource https://github.com/coccoc/coccoc-tokenizer

## License
GNU Lesser General Public License v3.0
