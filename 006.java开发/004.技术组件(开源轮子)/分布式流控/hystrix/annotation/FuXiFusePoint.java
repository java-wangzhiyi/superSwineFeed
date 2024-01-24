//package com.jd.jrdp.exts.server.annotation;
//
//import com.jd.fastjson.JSON;
//import com.jd.fastjson.JSONObject;
//import com.jd.jrdp.exts.server.constants.BizConst;
//import com.jd.jrdp.exts.server.constants.CommonConst;
//import com.jd.jrdp.exts.server.constants.DefaultCommonConst;
//import com.jd.jrdp.exts.server.constants.FuXiFallbackDefaultResult;
//import com.jd.jrdp.exts.server.service.jdt.DataFuXiService;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@Aspect
//@Component
//public class FuXiFusePoint {
//
//    @Pointcut("@annotation(com.jd.jrdp.exts.server.annotation.FuXiFuse)")
//    public void annotationPointCut(){};
//
//    @Around("annotationPointCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
//        Object[] args = joinPoint.getArgs();
//        Object o = Objects.nonNull(args) ? ((Map)args[0]).get(BizConst.OPTYPE) : "";
//        Object isfuse = DefaultCommonConst.interIdList.get(BizConst.HYSTRIX + o);
//        boolean flag = Objects.nonNull(isfuse) ? ((Integer) isfuse == 1) : false;
//        if (!flag){
//            try {
//                joinPoint.proceed(JSON.parseObject(args[0].toString(), (Type) Map.class));
//            }catch (Throwable e){
////                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
////                stringObjectHashMap.put("msg","error");
////                return stringObjectHashMap;
////                return JSON.parseObject(args[0].toString(),Map.class);
//                Map interIdList = DefaultCommonConst.interIdList.get(o);
//                if (Objects.nonNull(interIdList)) {
//                    String defaultResult = String.valueOf(interIdList.get(CommonConst.DEFAULT_RESULT));
//                    return StringUtils.isBlank(defaultResult) ? FuXiFallbackDefaultResult.ALL_DEFAULT_RESULT : JSONObject.parseObject(defaultResult, Map.class);
//                }
//                return FuXiFallbackDefaultResult.ALL_DEFAULT_RESULT;
//            }
//        }
//        return joinPoint.proceed();
//    }
//}
//
//
