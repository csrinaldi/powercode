/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.workinflow.tool.annprocesor;

import java.io.File;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 *
 * @author csrinaldi
 */
@SupportedAnnotationTypes("com.workinflow.tool.annprocesor.Resource")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ResourceProcesor extends AbstractProcessor{

    public ResourceProcesor(){
        
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment re) {
        
        Set<? extends Element> elements = re.getElementsAnnotatedWith(Resource.class);
        
        for (Element e : elements){
            if ( e.getKind() == ElementKind.INTERFACE ){
                Resource resource = e.getAnnotation(Resource.class);
                String path = resource.resourcePath();
                String name = e.getSimpleName().toString();
                System.out.println("Name "+name);
                System.out.println("Path "+path);
                File file = new File(path);
                if ( file.exists() ){
                    System.out.println("Existe!!!");
                    if ( file.isDirectory() ){
                        System.out.println("Es un directorio");
                    }
                }
                

                
                
            }
        }
        
        return true;
    }
    
}
