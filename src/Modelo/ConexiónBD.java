package Modelo;
import java.sql.*;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class ConexiónBD {
    protected Connection con;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_GENERAL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String URL_INVENTARIO = "jdbc:mysql://localhost:3306/proyecto?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";

    public void conectar() throws ClassNotFoundException {
        try {
            Class.forName(DRIVER);
            boolean baseRecienCreada = false;
            Connection tempCon = DriverManager.getConnection(URL_GENERAL, USUARIO, CONTRASEÑA);
            Statement st = tempCon.createStatement();
            ResultSet rs = st.executeQuery("SHOW DATABASES LIKE 'proyecto'");
            if (!rs.next()) {
                st.executeUpdate("CREATE DATABASE proyecto");
                baseRecienCreada = true;
            }
            rs.close();
            st.close();
            tempCon.close();
            con = DriverManager.getConnection(URL_INVENTARIO, USUARIO, CONTRASEÑA);
            crearTablas();
            if (baseRecienCreada) {
                crearTriggers();
                crearDatosIniciales();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error al conectar con la base de datos:\n" +
                "Asegúrese de que MySQL esté ejecutándose.\n\n" +
                "Detalles: " + e.getMessage(),
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cerrarCn() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    private void crearTablas() {
        String sql = """
            CREATE TABLE IF NOT EXISTS almacen (
              id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
              pasillo VARCHAR(50) NOT NULL,
              ala ENUM('IZQUIERDA','DERECHA') NOT NULL,
              estante INT NOT NULL,
              nivel INT NOT NULL,
              capacidad INT DEFAULT NULL,
              activa ENUM('SI','NO') DEFAULT 'SI'
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS categoria (
              idCategoria INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(50) UNIQUE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS marca (
              idMarca INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(50) UNIQUE,
              paisOrigen VARCHAR(50)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS proveedor (
              idProveedor INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(100),
              telefono VARCHAR(20) UNIQUE,
              direccion VARCHAR(100),
              email VARCHAR(100) UNIQUE,
              RFC VARCHAR(20)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS cuenta (
              id_cuenta INT AUTO_INCREMENT PRIMARY KEY,
              codigo VARCHAR(20) UNIQUE NOT NULL,
              nombre VARCHAR(100) NOT NULL,
              tipo ENUM('ACTIVO','PASIVO','PATRIMONIO','INGRESO','GASTO') NOT NULL,
              descripcion VARCHAR(255),
              saldo_inicial DECIMAL(18,2) DEFAULT 0.00
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS empleado (
              idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(50),
              apellido VARCHAR(50),
              cedula VARCHAR(15),
              fecha_nacimiento DATE,
              email VARCHAR(100),
              telefono VARCHAR(20),
              cargo VARCHAR(50)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS usuario (
              idUsuario INT AUTO_INCREMENT PRIMARY KEY,
              nombreUsuario VARCHAR(50),
              clave VARCHAR(255),
              nivel_acceso VARCHAR(20),
              estado VARCHAR(45) NOT NULL,
              idEmpleado INT,
              FOREIGN KEY (idEmpleado) REFERENCES empleado(idEmpleado) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS producto (
              idProducto INT AUTO_INCREMENT PRIMARY KEY,
              nombre VARCHAR(100),
              tipo_Liquido VARCHAR(45),
              viscosidad VARCHAR(45),
              presentacion VARCHAR(45),
              condicion VARCHAR(45),
              compatibilidad VARCHAR(200),
              numero_serial VARCHAR(45),
              unidad_De_Medidad VARCHAR(45),
              especificaciones VARCHAR(150),
              densidad VARCHAR(45),
              idCategoria INT NOT NULL,
              idMarca INT NOT NULL,
              tipo_producto ENUM('SOLIDO','LIQUIDO','UNIDAD'),
              FOREIGN KEY (idCategoria) REFERENCES categoria(idCategoria),
              FOREIGN KEY (idMarca) REFERENCES marca(idMarca)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS inventario (
              id_inventario INT AUTO_INCREMENT PRIMARY KEY,
              idProducto INT,
              stockMinimo INT,
              Cantidad INT,
              precio_unitario DECIMAL(18,2) NOT NULL,
              stockMaximo INT,
              lote VARCHAR(45),
              fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              Fecha_Vencimiento VARCHAR(45),
              id_Ubicacion INT,
              costo_total DECIMAL(18,2),
              Cantidad_Disponible INT NOT NULL DEFAULT 0,
              tipo_producto ENUM('LIQUIDO','SOLIDO','UNIDAD') NOT NULL,
              Usuario VARCHAR(45),
              precio_venta DECIMAL(18,2),
              porcentaje INT,
              id_cuenta_inventario INT,
              id_cuenta_proveedor INT,
              idProveedor INT NOT NULL,
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto) ON UPDATE CASCADE,
              FOREIGN KEY (id_Ubicacion) REFERENCES almacen(id_ubicacion),
              FOREIGN KEY (id_cuenta_inventario) REFERENCES cuenta(id_cuenta),
              FOREIGN KEY (id_cuenta_proveedor) REFERENCES cuenta(id_cuenta),
               FOREIGN KEY (idProveedor) REFERENCES proveedor(idProveedor)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS asiento_contable (
              id_asiento INT AUTO_INCREMENT PRIMARY KEY,
              fecha DATE DEFAULT (CURDATE()),
              descripcion VARCHAR(255),
              referencia VARCHAR(50),
              total_debe DECIMAL(18,2) DEFAULT 0.00,
              total_haber DECIMAL(18,2) DEFAULT 0.00
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS libro_diario (
              id_libro_diario INT AUTO_INCREMENT PRIMARY KEY,
              id_asiento INT NOT NULL,
              id_cuenta INT NOT NULL,
              debe DECIMAL(18,2) DEFAULT 0.00,
              haber DECIMAL(18,2) DEFAULT 0.00,
              FOREIGN KEY (id_asiento) REFERENCES asiento_contable(id_asiento) ON DELETE CASCADE,
              FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS libro_mayor (
              id_libro_mayor INT AUTO_INCREMENT PRIMARY KEY,
              id_cuenta INT NOT NULL,
              fecha DATE NOT NULL,
              saldo_anterior DECIMAL(18,2) DEFAULT 0.00,
              debe DECIMAL(18,2) DEFAULT 0.00,
              haber DECIMAL(18,2) DEFAULT 0.00,
              saldo_final DECIMAL(18,2) DEFAULT 0.00,
              FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            CREATE TABLE IF NOT EXISTS movimiento (
              ID INT AUTO_INCREMENT PRIMARY KEY,
              idProducto INT NOT NULL,
              id_inventario INT,
              Tipo_Producto VARCHAR(100) NOT NULL,
              Movimiento ENUM('ENTRADA','AJUSTE','SALIDA') NOT NULL,
              Cantidad INT NOT NULL,
              StockFinal INT DEFAULT 0,
              Usuario VARCHAR(50) NOT NULL,
              Detalle TEXT,
              Precio_Venta DECIMAL(18,2),
              FechaMovimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
              id_cuenta INT,
              id_cuenta_ingreso INT,
              FOREIGN KEY (idProducto) REFERENCES producto(idProducto) ON UPDATE CASCADE,
              FOREIGN KEY (id_inventario) REFERENCES inventario(id_inventario) ON DELETE CASCADE ON UPDATE CASCADE,
              FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta),
              FOREIGN KEY (id_cuenta_ingreso) REFERENCES cuenta(id_cuenta)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
            """;
        try (Statement st = con.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS=0;");
            String[] sentencias = sql.split(";");
            for (String sentencia : sentencias) {
                sentencia = sentencia.trim();
                if (!sentencia.isEmpty()) {
                    st.execute(sentencia);
                }
            }
            st.execute("SET FOREIGN_KEY_CHECKS=1;");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear tablas:\n" + e.getMessage());
        }
    }

    private void crearTriggers() {
        String[] triggerNames = {
            "before_insert_inventario", "before_update_inventario",
            "tr_inventario_to_movimiento", "tr_movimiento_before_insert",
            "tr_movimiento_after_insert", "tr_movimiento_to_contabilidad",
            "tr_cuenta_inicial_mayor", "tr_libro_diario_to_libro_mayor",
            "set_tipo_producto",
            "trg_bloquear_eliminar_categoria", "trg_bloquear_eliminar_marca",
            "trg_bloquear_eliminar_producto", "trg_bloquear_eliminar_proveedor",
            "tr_update_inventario_contabilidad", "tr_prevent_duplicate_location_insert",
            "tr_actualizar_producto_en_movimientos",
            "tr_prevent_duplicate_location_update"
        };
        try (Statement st = con.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS=0;");
            for (String name : triggerNames) {
                try { st.execute("DROP TRIGGER IF EXISTS " + name); } catch (SQLException ignored) {}
            }

            // ==== TUS TRIGGERS ORIGINALES (SIN TOCAR NADA) ====
            st.execute("""
                CREATE TRIGGER before_insert_inventario
                BEFORE INSERT ON inventario
                FOR EACH ROW
                BEGIN
                    DECLARE ultimo_num INT DEFAULT 0;
                    DECLARE nuevo_num INT;
                    IF NEW.costo_total IS NULL OR NEW.costo_total = 0 THEN
                        SET NEW.costo_total = NEW.Cantidad * NEW.precio_unitario;
                    END IF;
                    SET NEW.Cantidad_Disponible = NEW.Cantidad;
                    SELECT IFNULL(MAX(CAST(SUBSTRING_INDEX(lote, 'prov', -1) AS UNSIGNED)), 0) INTO ultimo_num
                    FROM inventario WHERE lote LIKE '%prov%';
                    SET nuevo_num = ultimo_num + 1;
                    SET NEW.lote = CONCAT('Lot ', DATE_FORMAT(CURDATE(), '%Y-%m-%d'), ' prov', LPAD(nuevo_num, 3, '0'));
                END
                """);

            st.execute("""
                CREATE TRIGGER before_update_inventario
                BEFORE UPDATE ON inventario
                FOR EACH ROW
                BEGIN
                    IF NEW.precio_unitario <> OLD.precio_unitario OR NEW.Cantidad <> OLD.Cantidad THEN
                        SET NEW.costo_total = NEW.Cantidad * NEW.precio_unitario;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_inventario_to_movimiento
                AFTER INSERT ON inventario
                FOR EACH ROW
                BEGIN
                    DECLARE vTipoProducto VARCHAR(100);
                    DECLARE vTotal DECIMAL(18,2);
                    DECLARE vIdMovimiento INT;
                    DECLARE vNombreProducto VARCHAR(100);
                    SELECT tipo_producto INTO vTipoProducto
                    FROM producto WHERE idProducto = NEW.idProducto;
                    SELECT nombre INTO vNombreProducto
                    FROM producto WHERE idProducto = NEW.idProducto;
                    SET vTotal = NEW.Cantidad * NEW.precio_unitario;
                    SET @FROM_INVENTARIO = 1;
                    INSERT INTO movimiento (
                        idProducto, id_inventario, Tipo_Producto, Movimiento,
                        Cantidad, StockFinal, Usuario, Detalle, Precio_Venta,
                        id_cuenta
                    ) VALUES (
                        NEW.idProducto, NEW.id_inventario, vTipoProducto, 'ENTRADA',
                        NEW.Cantidad, NEW.Cantidad, NEW.Usuario,
                        CONCAT('Compra a proveedor - Lote: ', NEW.lote),
                        NEW.precio_venta, NEW.id_cuenta_proveedor
                    );
                    SET vIdMovimiento = LAST_INSERT_ID();
                    SET @FROM_INVENTARIO = NULL;
                    INSERT INTO asiento_contable (descripcion, referencia, total_debe, total_haber)
                    VALUES (CONCAT('Compra de ', vNombreProducto, ' - Lote ID: ', NEW.id_inventario), vIdMovimiento, vTotal, vTotal);
                    SET @id_asiento = LAST_INSERT_ID();
                    INSERT INTO libro_diario (id_asiento, id_cuenta, debe)
                    VALUES (@id_asiento, NEW.id_cuenta_inventario, vTotal);
                    INSERT INTO libro_diario (id_asiento, id_cuenta, haber)
                    VALUES (@id_asiento, NEW.id_cuenta_proveedor, vTotal);
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_movimiento_before_insert
                BEFORE INSERT ON movimiento
                FOR EACH ROW
                BEGIN
                    DECLARE vCantidadDisponible INT DEFAULT 0;
                    IF @FROM_INVENTARIO = 1 THEN
                        SET NEW.StockFinal = NEW.Cantidad;
                    ELSE
                        IF NEW.id_inventario IS NULL THEN
                            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'id_inventario es obligatorio para SALIDA o AJUSTE.';
                        END IF;
                        SELECT Cantidad_Disponible INTO vCantidadDisponible
                        FROM inventario WHERE id_inventario = NEW.id_inventario;
                        IF vCantidadDisponible IS NULL THEN
                            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Lote inválido.';
                        END IF;
                        IF NEW.Movimiento = 'SALIDA' AND NEW.Cantidad > vCantidadDisponible THEN
                            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente.';
                        END IF;
                        SET NEW.StockFinal = vCantidadDisponible +
                            (CASE WHEN NEW.Movimiento = 'SALIDA' THEN -NEW.Cantidad ELSE NEW.Cantidad END);
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_movimiento_after_insert
                AFTER INSERT ON movimiento
                FOR EACH ROW
                BEGIN
                    IF @FROM_INVENTARIO IS NULL AND NEW.id_inventario IS NOT NULL THEN
                        UPDATE inventario SET Cantidad_Disponible = NEW.StockFinal WHERE id_inventario = NEW.id_inventario;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_movimiento_to_contabilidad
                AFTER INSERT ON movimiento
                FOR EACH ROW
                BEGIN
                    DECLARE vTotal, vCosto DECIMAL(18,2);
                    DECLARE vCuentaInventario, vPrecioUnitario DECIMAL(18,2);
                    DECLARE vNombreProducto VARCHAR(100);
                    DECLARE vLote VARCHAR(45);
                 
                    SELECT precio_unitario, id_cuenta_inventario
                    INTO vPrecioUnitario, vCuentaInventario
                    FROM inventario WHERE id_inventario = NEW.id_inventario;
                 
                    SELECT nombre INTO vNombreProducto
                    FROM producto WHERE idProducto = NEW.idProducto;
                 
                    SELECT lote INTO vLote
                    FROM inventario WHERE id_inventario = NEW.id_inventario;
                 
                    IF NEW.Movimiento = 'SALIDA' THEN
                        SET vTotal = NEW.Cantidad * NEW.Precio_Venta;
                        SET vCosto = NEW.Cantidad * vPrecioUnitario;
                     
                        INSERT INTO asiento_contable (descripcion, referencia, total_debe, total_haber)
                        VALUES (CONCAT('Venta de ', vNombreProducto, ' - Lote: ', vLote), NEW.ID, vTotal + vCosto, vTotal + vCosto);
                        SET @id_asiento = LAST_INSERT_ID();
                     
                        INSERT INTO libro_diario (id_asiento, id_cuenta, debe)
                        VALUES (@id_asiento, NEW.id_cuenta, vTotal);
                     
                        INSERT INTO libro_diario (id_asiento, id_cuenta, haber)
                        VALUES (@id_asiento, NEW.id_cuenta_ingreso, vTotal);
                     
                        INSERT INTO libro_diario (id_asiento, id_cuenta, debe)
                        VALUES (@id_asiento, 5, vCosto);
                     
                        INSERT INTO libro_diario (id_asiento, id_cuenta, haber)
                        VALUES (@id_asiento, vCuentaInventario, vCosto);
                     
                    ELSEIF NEW.Movimiento = 'AJUSTE' THEN
                        SET vTotal = ABS(NEW.Cantidad) * vPrecioUnitario;
                        INSERT INTO asiento_contable (descripcion, referencia, total_debe, total_haber)
                        VALUES (CONCAT('Ajuste de ', vNombreProducto, ' - Lote: ', vLote), NEW.ID, vTotal, vTotal);
                        SET @id_asiento = LAST_INSERT_ID();
                     
                        IF NEW.Cantidad > 0 THEN
                            INSERT INTO libro_diario (id_asiento, id_cuenta, debe) VALUES (@id_asiento, vCuentaInventario, vTotal);
                            INSERT INTO libro_diario (id_asiento, id_cuenta, haber) VALUES (@id_asiento, NEW.id_cuenta, vTotal);
                        ELSE
                            INSERT INTO libro_diario (id_asiento, id_cuenta, debe) VALUES (@id_asiento, NEW.id_cuenta, vTotal);
                            INSERT INTO libro_diario (id_asiento, id_cuenta, haber) VALUES (@id_asiento, vCuentaInventario, vTotal);
                        END IF;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER set_tipo_producto BEFORE INSERT ON producto FOR EACH ROW
                BEGIN
                    IF NEW.tipo_Liquido IS NOT NULL AND NEW.tipo_Liquido <> '' THEN
                        SET NEW.tipo_producto = 'LIQUIDO';
                    ELSEIF NEW.condicion IS NOT NULL AND NEW.condicion <> '' THEN
                        SET NEW.tipo_producto = 'SOLIDO';
                    ELSEIF (NEW.especificaciones IS NOT NULL AND NEW.especificaciones <> '')
                       AND (NEW.numero_serial IS NOT NULL AND NEW.numero_serial <> '') THEN
                        SET NEW.tipo_producto = 'UNIDAD';
                    ELSE
                        SET NEW.tipo_producto = NULL;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_cuenta_inicial_mayor AFTER INSERT ON cuenta FOR EACH ROW
                BEGIN
                    IF NOT EXISTS (SELECT 1 FROM libro_mayor WHERE id_cuenta = NEW.id_cuenta) THEN
                        INSERT INTO libro_mayor (id_cuenta, fecha, saldo_anterior, debe, haber, saldo_final)
                        VALUES (
                            NEW.id_cuenta, CURDATE(), 0,
                            IF(NEW.tipo IN ('ACTIVO', 'GASTO'), NEW.saldo_inicial, 0),
                            IF(NEW.tipo IN ('PASIVO', 'PATRIMONIO', 'INGRESO'), NEW.saldo_inicial, 0),
                            NEW.saldo_inicial
                        );
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_libro_diario_to_libro_mayor
                AFTER INSERT ON libro_diario
                FOR EACH ROW
                BEGIN
                    DECLARE vFecha DATE;
                    DECLARE vSaldoAnterior, vDelta DECIMAL(18,2);
                    DECLARE vTipoCuenta VARCHAR(20);
                 
                    SELECT fecha INTO vFecha FROM asiento_contable WHERE id_asiento = NEW.id_asiento;
                 
                    SELECT tipo INTO vTipoCuenta FROM cuenta WHERE id_cuenta = NEW.id_cuenta;
                 
                    IF vTipoCuenta IN ('INGRESO', 'PATRIMONIO', 'PASIVO') THEN
                        SET vDelta = IFNULL(NEW.haber, 0) - IFNULL(NEW.debe, 0);
                    ELSE
                        SET vDelta = IFNULL(NEW.debe, 0) - IFNULL(NEW.haber, 0);
                    END IF;
                 
                    SELECT IFNULL(saldo_final, 0) INTO vSaldoAnterior
                    FROM libro_mayor
                    WHERE id_cuenta = NEW.id_cuenta AND fecha < vFecha
                    ORDER BY fecha DESC, id_libro_mayor DESC LIMIT 1;
                 
                    IF EXISTS (SELECT 1 FROM libro_mayor WHERE id_cuenta = NEW.id_cuenta AND fecha = vFecha) THEN
                        UPDATE libro_mayor
                        SET debe = debe + IFNULL(NEW.debe, 0),
                            haber = haber + IFNULL(NEW.haber, 0),
                            saldo_final = saldo_final + vDelta
                        WHERE id_cuenta = NEW.id_cuenta AND fecha = vFecha;
                     
                        UPDATE libro_mayor
                        SET saldo_anterior = saldo_anterior + vDelta,
                            saldo_final = saldo_final + vDelta
                        WHERE id_cuenta = NEW.id_cuenta AND fecha > vFecha;
                    ELSE
                        INSERT INTO libro_mayor (id_cuenta, fecha, saldo_anterior, debe, haber, saldo_final)
                        VALUES (NEW.id_cuenta, vFecha, vSaldoAnterior, IFNULL(NEW.debe, 0), IFNULL(NEW.haber, 0), vSaldoAnterior + vDelta);
                     
                        UPDATE libro_mayor
                        SET saldo_anterior = saldo_anterior + vDelta,
                            saldo_final = saldo_final + vDelta
                        WHERE id_cuenta = NEW.id_cuenta AND fecha > vFecha;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER trg_bloquear_eliminar_categoria BEFORE DELETE ON categoria FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM producto WHERE idCategoria = OLD.idCategoria) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar la categoría: hay productos asignados.';
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER trg_bloquear_eliminar_marca BEFORE DELETE ON marca FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM producto WHERE idMarca = OLD.idMarca) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar la marca: hay productos asignados.';
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER trg_bloquear_eliminar_producto BEFORE DELETE ON producto FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM inventario WHERE idProducto = OLD.idProducto AND Cantidad_Disponible > 0) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar el producto: tiene stock en inventario.';
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER trg_bloquear_eliminar_proveedor BEFORE DELETE ON proveedor FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM producto p JOIN inventario i ON p.idProducto = i.idProducto
                               WHERE p.idProveedor = OLD.idProveedor AND i.Cantidad_Disponible > 0) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar el proveedor: tiene productos con stock.';
                    END IF;
                END
                """);

            st.execute("CREATE TRIGGER tr_update_inventario_contabilidad " +
                "AFTER UPDATE ON inventario FOR EACH ROW " +
                "BEGIN " +
                " DECLARE vDiferencia DECIMAL(18,2); " +
                " DECLARE vAsientoID INT DEFAULT NULL; " +
                " DECLARE vFechaAsiento DATE DEFAULT NULL; " +
                " DECLARE vCuentaContrapartida INT DEFAULT NULL; " +
                " DECLARE vTipoContrapartida VARCHAR(20); " +
                "" +
                " SET vDiferencia = (NEW.costo_total) - (OLD.costo_total); " +
                "" +
                " IF vDiferencia <> 0 AND NEW.id_cuenta_inventario IS NOT NULL THEN " +
                "" +
                " SELECT m.id_cuenta, c.tipo INTO vCuentaContrapartida, vTipoContrapartida " +
                " FROM movimiento m " +
                " JOIN cuenta c ON m.id_cuenta = c.id_cuenta " +
                " WHERE m.id_inventario = NEW.id_inventario AND m.Movimiento = 'ENTRADA' " +
                " ORDER BY m.ID DESC LIMIT 1; " +
                "" +
                " IF vCuentaContrapartida IS NOT NULL THEN " +
                " SELECT ac.id_asiento, ac.fecha INTO vAsientoID, vFechaAsiento " +
                " FROM asiento_contable ac " +
                " JOIN movimiento m ON ac.referencia = CAST(m.ID AS CHAR) " +
                " WHERE m.id_inventario = NEW.id_inventario AND m.Movimiento = 'ENTRADA' " +
                " ORDER BY m.ID DESC LIMIT 1; " +
                "" +
                " IF vAsientoID IS NOT NULL THEN " +
                " UPDATE asiento_contable SET total_debe = total_debe + vDiferencia, total_haber = total_haber + vDiferencia WHERE id_asiento = vAsientoID; " +
                " UPDATE libro_diario SET debe = debe + vDiferencia WHERE id_asiento = vAsientoID AND id_cuenta = NEW.id_cuenta_inventario; " +
                " UPDATE libro_diario SET haber = haber + vDiferencia WHERE id_asiento = vAsientoID AND id_cuenta = vCuentaContrapartida; " +
                "" +
                " UPDATE libro_mayor SET debe = debe + vDiferencia, saldo_final = saldo_final + vDiferencia WHERE id_cuenta = NEW.id_cuenta_inventario AND fecha >= vFechaAsiento; " +
                "" +
                " IF vTipoContrapartida = 'ACTIVO' THEN " +
                " UPDATE libro_mayor SET haber = haber + vDiferencia, saldo_final = saldo_final - vDiferencia WHERE id_cuenta = vCuentaContrapartida AND fecha >= vFechaAsiento; " +
                " ELSEIF vTipoContrapartida = 'PASIVO' THEN " +
                " UPDATE libro_mayor SET haber = haber + vDiferencia, saldo_final = saldo_final + vDiferencia WHERE id_cuenta = vCuentaContrapartida AND fecha >= vFechaAsiento; " +
                " END IF; " +
                " END IF; " +
                " END IF; " +
                " END IF; " +
                "END");

            
            st.execute("""
    CREATE TRIGGER tr_prevent_duplicate_location_insert
    BEFORE INSERT ON inventario
    FOR EACH ROW
    BEGIN
        IF NEW.id_Ubicacion IS NOT NULL THEN
            IF EXISTS (SELECT 1 FROM inventario WHERE id_Ubicacion = NEW.id_Ubicacion AND id_inventario != NEW.id_inventario) THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'ERROR: Esta ubicación ya está ocupada por otro lote.';
            END IF;
        END IF;
    END
    """);
            
     st.execute("""
    CREATE TRIGGER tr_prevent_duplicate_location_update
    BEFORE UPDATE ON inventario
    FOR EACH ROW
    BEGIN
        IF NEW.id_Ubicacion IS NOT NULL 
           AND (OLD.id_Ubicacion IS NULL OR OLD.id_Ubicacion != NEW.id_Ubicacion) THEN
           
            IF EXISTS (
                SELECT 1 FROM inventario 
                WHERE id_Ubicacion = NEW.id_Ubicacion 
                  AND id_inventario != OLD.id_inventario
                  AND Cantidad_Disponible > 0
            ) THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'ERROR: Esta ubicación ya está ocupada por otro lote con stock. Elige otra ubicación libre.';
            END IF;
        END IF;
    END
    """);
            // ==== LOS 5 NUEVOS TRIGGERS DE UBICACIÓN (AGREGADOS AHORA) ====
            st.execute("""
                CREATE TRIGGER tr_prevent_duplicate_location
                BEFORE INSERT ON inventario
                FOR EACH ROW
                BEGIN
                    IF NEW.id_Ubicacion IS NOT NULL THEN
                        IF EXISTS (SELECT 1 FROM inventario WHERE id_Ubicacion = NEW.id_Ubicacion) THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'ERROR: Esta ubicación ya está ocupada por otro lote. Elija otra ubicación.';
                        END IF;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_check_capacity_on_insert
                BEFORE INSERT ON inventario
                FOR EACH ROW
                BEGIN
                    DECLARE cap INT;
                    IF NEW.id_Ubicacion IS NOT NULL THEN
                        SELECT capacidad INTO cap FROM almacen WHERE id_ubicacion = NEW.id_Ubicacion;
                        IF cap IS NOT NULL AND NEW.Cantidad_Disponible > cap THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'ERROR: La cantidad ingresada excede la capacidad máxima de esta ubicación.';
                        END IF;
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER tr_check_capacity_on_update
                BEFORE UPDATE ON inventario
                FOR EACH ROW
                BEGIN
                    DECLARE cap INT;
                    IF NEW.id_Ubicacion IS NOT NULL AND NEW.Cantidad_Disponible != OLD.Cantidad_Disponible THEN
                        SELECT capacidad INTO cap FROM almacen WHERE id_ubicacion = NEW.id_Ubicacion;
                        IF cap IS NOT NULL AND NEW.Cantidad_Disponible > cap THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'ERROR: La nueva cantidad excede la capacidad máxima de esta ubicación.';
                        END IF;
                    END IF;
                END
                """);

 // === TRIGGER QUE LIBERA LA UBICACIÓN (AHORA SÍ FUNCIONA) ===
//st.execute("DROP TRIGGER IF EXISTS tr_deactivate_location_on_zero;");

st.execute("""
    CREATE TRIGGER tr_deactivate_location_on_zero
    BEFORE UPDATE ON inventario
    FOR EACH ROW
    BEGIN
        IF NEW.Cantidad_Disponible = 0 
           AND OLD.Cantidad_Disponible > 0 
           AND OLD.id_Ubicacion IS NOT NULL THEN
            SET NEW.id_Ubicacion = NULL;
        END IF;
    END
    """);

// === TRIGGER QUE BLOQUEA AJUSTE POSITIVO ===
//st.execute("DROP TRIGGER IF EXISTS tr_block_positive_adjust_inactive_location;");

st.execute("""
    CREATE TRIGGER tr_block_positive_adjust_inactive_location
    BEFORE INSERT ON movimiento
    FOR EACH ROW
    BEGIN
        DECLARE ubicacion_actual INT DEFAULT NULL;
        
        IF NEW.Movimiento = 'AJUSTE' AND NEW.Cantidad > 0 AND NEW.id_inventario IS NOT NULL THEN
            SELECT id_Ubicacion INTO ubicacion_actual
            FROM inventario
            WHERE id_inventario = NEW.id_inventario;
            
            IF ubicacion_actual IS NULL THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'ERROR: Ubicación desactivada. Asigna una nueva ubicación activa antes de hacer ajuste positivo.';
            END IF;
        END IF;
    END
    """);

st.execute("""
    CREATE TRIGGER tr_actualizar_producto_en_movimientos
    AFTER UPDATE ON inventario
    FOR EACH ROW
    BEGIN
        -- Solo si cambió el producto
        IF NEW.idProducto <> OLD.idProducto THEN
            
            UPDATE movimiento m
            JOIN producto p ON p.idProducto = NEW.idProducto
            SET m.idProducto     = NEW.idProducto,
                m.Tipo_Producto   = p.tipo_producto,
                m.Detalle         = CONCAT('Compra a proveedor - Lote: ', NEW.lote, ' - ', p.nombre)
            WHERE m.id_inventario = NEW.id_inventario;
            
        END IF;
    END
    """);
            st.execute("SET FOREIGN_KEY_CHECKS=1;");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear triggers:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearDatosIniciales() {
        try {
            String sql = "INSERT INTO empleado (nombre, apellido, cedula, fecha_nacimiento, email, telefono, cargo) " +
                         "VALUES ('Admin', 'Sistema', '0000000000', '2000-01-01', 'bot@sistema.com', '0000000000', 'Dueño')";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            int idEmpleado = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) idEmpleado = rs.getInt(1);
            rs.close();
            ps.close();
            String contrasenaPlana = "inventario.01";
            String contrasenaCifrada = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(12));
            sql = "INSERT INTO usuario (nombreUsuario, clave, nivel_acceso, estado, idEmpleado) " +
                  "VALUES ('Admin', ?, 'Alto', 'Activo', ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, contrasenaCifrada);
            ps.setInt(2, idEmpleado);
            ps.executeUpdate();
            ps.close();
            String[] cuentas = {
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('1.1.1.1', 'Caja', 'ACTIVO', 'Dinero en efectivo')",
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('1.1.3.1', 'Inventario', 'ACTIVO', 'Valor de productos')",
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('2.1.1.1', 'Proveedores', 'PASIVO', 'Deudas con proveedores')",
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('4.1.1.1', 'Ventas', 'INGRESO', 'Ingresos por ventas')",
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('5.2.1.1', 'Costo de Ventas', 'GASTO', 'Costo de productos vendidos')",
                "INSERT IGNORE INTO cuenta (codigo, nombre, tipo, descripcion) VALUES ('5.9.1.1', 'Ajustes de Inventario', 'GASTO', 'Pérdidas o sobrantes')"
            };
            Statement st = con.createStatement();
            for (String c : cuentas) {
                st.execute(c);
            }
            st.close();
            JOptionPane.showMessageDialog(null,
                "Base de datos 'proyecto' creada con éxito.\n\n" +
                "Usuario: Admin | Contraseña: inventario.01",
                "Sistema Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear datos iniciales:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean estaConectado() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return con;
    }
}