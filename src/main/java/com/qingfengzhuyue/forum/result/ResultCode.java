package com.qingfengzhuyue.forum.result;

public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),

    QUESTION_NOT_FOUND(2001,"你找到问题不存在了，要不换一个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请先登录后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "你找到评论不存在了，要不换一个试试？"),
    COMMENT_IS_EMPTY(2007, "输入内容不能为空！"),

    NO_SUFFIX(2008,"上传图片格式不正确"),
    NOT_A_PICTURE(2009,"上传的不是图片,请上传图片!"),
    DELETE_PICTURE_SUCCEEDED(2010,"删除图片成功 "),

    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
