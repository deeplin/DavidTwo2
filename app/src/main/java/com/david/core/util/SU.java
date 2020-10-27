package com.david.core.util;

/**
 * su执行命令工具类.
 */

public final class SU {

    private SU() {
    }

    public static void close(java.io.Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (java.io.IOException e) {
            }
        }
    }

    /**
     * 使用su执行一条命令
     *
     * @param command 要执行的命令
     * @return 返回
     */
    public static String exec(String command) {
        return SU.exec(command, null);
    }

    /**
     * 使用su执行多条命令
     *
     * @param commands 要执行的命令
     * @return 返回
     */
    public static String exec(String commands[]) {
        StringBuilder build = new StringBuilder();
        for (String cmd : commands) {
            build.append(SU.exec(cmd, null));
        }
        return build.toString();
    }

    /**
     * 使用su在指定工作目录下执行一条命令
     *
     * @param command          要执行的命令
     * @param workingDirectory 工作目录
     * @return 返回
     */
    public static String exec(String command, java.io.File workingDirectory) {
        if (command == null || (command = command.trim()).length() == 0)
            return null;
        if (workingDirectory == null)
            workingDirectory = new java.io.File("/");
        java.io.OutputStream out = null;
        java.io.InputStream in = null;
        java.io.InputStream err = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("su", null, workingDirectory);
            StringBuffer inString = new StringBuffer();
            StringBuffer errString = new StringBuffer();
            out = process.getOutputStream();

            out.write(command.endsWith("\n") ? command.getBytes() : (command + "\n").getBytes());
            out.write(new byte[]{'e', 'x', 'i', 't', '\n'});
            in = process.getInputStream();
            err = process.getErrorStream();
            while (in.available() > 0)
                inString.append((char) in.read());
            while (err.available() > 0)
                errString.append((char) err.read());
            return inString.toString();
        } catch (Exception ioex) {
            return null;
        } finally {
            SU.close(out);
            SU.close(in);
            SU.close(err);
        }
    }
}