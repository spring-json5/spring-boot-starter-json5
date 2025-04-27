package io.github.spring.json5;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Json5Mapper extends JsonMapper {
    public Json5Mapper() {
        enable(
                JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(),
                JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(),
                JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(),
                JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(),
                JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature(),
                JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature(),
                JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature(),
                JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS.mappedFeature(),
                JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature()
        );
        disable(
                JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(),
                JsonWriteFeature.WRITE_NAN_AS_STRINGS.mappedFeature()
        );
    }

    public static JsonMapper build() {
        return JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .disable(JsonWriteFeature.QUOTE_FIELD_NAMES)
                .disable(JsonWriteFeature.WRITE_NAN_AS_STRINGS)
                .build();
    }
}