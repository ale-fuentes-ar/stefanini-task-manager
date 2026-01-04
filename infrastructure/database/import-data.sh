for i in {1..50};
do
    /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "Stefanini2025!" -C -i /scripts/setup.sql
    if [ $? -eq 0 ]
    then
        echo "Capa de Datos: Base de datos stefaninidb creada con éxito."
        break
    else
        echo "Capa de Datos: SQL Server aún no está listo para crear la base de datos... reintentando (intento $i)"
        sleep 5
    fi
done