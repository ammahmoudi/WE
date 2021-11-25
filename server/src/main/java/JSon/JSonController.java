package JSon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import db.DBTools;

import java.io.IOException;

public
class JSonController {
static     ObjectMapper objectMapper = new ObjectMapper();
static Hibernate5Module hibernate5Module=new Hibernate5Module(null,DBTools.getSessionFactory());

    public static String objectToStringMapper(Object object){
        objectMapper.registerModule(new JSR310Module());
        //hibernate5Module.enable(Hibernate5Module.Feature.WRAP_IDENTIFIER_IN_OBJECT);
        hibernate5Module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
       // hibernate5Module.enable(Hibernate5Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL);
       //hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);
        String string = new String();

        try {
            string = objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }
    public static <T> T stringToObjectMapper(String string,Class<T> type){
      hibernate5Module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
       hibernate5Module.enable(Hibernate5Module.Feature.WRAP_IDENTIFIER_IN_OBJECT);
        //hibernate5Module.enable(Hibernate5Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL);
    //    hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);
        try {
            return objectMapper.readValue(string,type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static <T> T stringToObjectMapper(String string, TypeReference <T> valueTypeRef){
        hibernate5Module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        hibernate5Module.enable(Hibernate5Module.Feature.WRAP_IDENTIFIER_IN_OBJECT);
        //hibernate5Module.enable(Hibernate5Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL);
        //    hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);
        try {
            return objectMapper.readValue(string,valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
