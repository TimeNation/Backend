package net.timenation.backend.mysql

import com.mysql.cj.jdbc.MysqlDataSource
import net.timenation.backend.Backend
import net.timenation.backend.config.BackendConfig
import net.timenation.backend.logger.LogType
import net.timenation.backend.logger.Logger
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MySQL {

    private var host: String? = null
    private var user: String? = null
    private var password: String? = null
    private var database: String? = null
    private var port = 0
    private var connection: Connection? = null
    private var executorService: ExecutorService? = null

    init {
        host = "127.0.0.1"
        port = 3306
        user = "admin"
        password = Backend.instance.timeConfig.mysqlPassword
        database = "timenation"
        executorService = Executors.newCachedThreadPool()
        connectToDatabase()
    }

    fun connectToDatabase() {
        if (!isConnectedToDatabase()) {
            try {
                val mysqlDataSource = MysqlDataSource()
                mysqlDataSource.serverName = host
                mysqlDataSource.port = port
                mysqlDataSource.user = user
                mysqlDataSource.password = password;
                mysqlDataSource.databaseName = database

                connection = mysqlDataSource.connection
                Logger.log("MySQL connection is connected", LogType.SUCCESSFULLY)
            } catch (sqlException: SQLException) {
                sqlException.printStackTrace()
                Logger.log("MySQL connection has not connected. ERROR #" + sqlException.errorCode, LogType.ERROR)
            }
        }
    }

    fun isConnectedToDatabase(): Boolean {
        return connection != null
    }

    fun disconnectFromDatabase() {
        if (isConnectedToDatabase()) {
            try {
                connection!!.close()
                Logger.log("MySQL connection was closed.", LogType.SUCCESSFULLY)
            } catch (sqlException: SQLException) {
                sqlException.printStackTrace()
            }
        }
    }

    fun getConnectionToDatabase(): Connection? {
        return connection
    }

    fun updateDatabase(query: String?) {
        executorService!!.execute {
            var preparedStatement: PreparedStatement? = null
            try {
                preparedStatement = connection!!.prepareStatement(query)
                preparedStatement.executeUpdate()
            } catch (var11: SQLException) {
                var11.printStackTrace()
            } finally {
                try {
                    preparedStatement!!.close()
                } catch (var10: SQLException) {
                    var10.printStackTrace()
                }
            }
        }
    }

    fun getDatabaseResult(query: String?): ResultSet? {
        val preparedStatement: PreparedStatement
        val resultSet: ResultSet
        return try {
            preparedStatement = connection!!.prepareStatement(query)
            resultSet = preparedStatement.executeQuery()
            resultSet
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
            null
        }
    }
}