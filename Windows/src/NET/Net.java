package NET;

import Base.ErrorCode;
import LOG.LogLevel;
import LOG.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class Net {
    private ServerSocket ServerSocket;
    private final ConcurrentHashMap<Integer, Socket> DeviceSocket = new ConcurrentHashMap<>();
    private final Logger logger;

    public Net() {
        logger = new Logger(NetBase.File_NetLog, this);

        try {
            ServerSocket = new ServerSocket(NetBase.NetProt_TransIn);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.ServerSocket(), "Create ServerSocket Failed!", e);
            System.exit(ErrorCode.Net.CreateSocketFailed);
        }

        Thread netMainThread = new Thread(() -> {
            try {
                int deviceIndex = 0;
                while (true) {
                    Socket socket = ServerSocket.accept();
                    deviceIndex++;

                    DeviceSocket.put(deviceIndex, socket);
                    logger.printAndWrite(LogLevel.INFO, new NetBase.Tags.Network(), "Device " + deviceIndex + " Connected!");

                    final int currentDeviceId = deviceIndex;
                    Thread handlerThread = new Thread(() -> {
                        handleDeviceConnection(currentDeviceId, socket);
                    });
                    handlerThread.start();
                }
            } catch (IOException e) {
                logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.Network(), "Server Socket Error", e);
            }
        });
        netMainThread.start();
    }

    private void handleDeviceConnection(int deviceId, Socket socket) {
        try {
            logger.printAndWrite(LogLevel.STEP, new NetBase.Tags.Network(), "Device " + deviceId + " handler started.");

            socket.setKeepAlive(true);

            OutputStream socketOutput = socket.getOutputStream();

            socketOutput.write(deviceId);
            socketOutput.flush();

            logger.printAndWrite(LogLevel.STEP, new NetBase.Tags.Network(), "Sent ID " + deviceId + " to device.");

        } catch (SocketException e) {
            logger.printAndWrite(LogLevel.FATAL, new NetBase.Tags.Network(), "Device " + deviceId + " Connection Error", e);
        } catch (IOException e) {
            logger.printAndWrite(LogLevel.ERROR, new NetBase.Tags.Network(), "Device " + deviceId + " Failed to send message", e);
        }
    }

    public static void main(String[] args) {
        new Net();
    }
}
