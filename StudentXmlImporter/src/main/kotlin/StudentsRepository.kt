import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class StudentsRepository(
    private val jdbcUrl: String,
    private val dbUser: String,
    private val dbPass: String
) {
    init {
        setupTables();
    }
    private fun getConnection(): Connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)

    private fun setupTables() {
        val sql = """
            CREATE TABLE IF NOT EXISTS students (
                id SERIAL PRIMARY KEY,
                first_name VARCHAR(100),
                last_name VARCHAR(100)
            );
            CREATE TABLE IF NOT EXISTS skills (
                id SERIAL PRIMARY KEY,
                skill_name VARCHAR(100) UNIQUE,
                is_hard BOOLEAN,
                is_soft BOOLEAN
            );
            CREATE TABLE IF NOT EXISTS student_to_skills (
                student_id INT REFERENCES students(id) ON DELETE CASCADE,
                skill_id INT REFERENCES skills(id) ON DELETE CASCADE,
                PRIMARY KEY (student_id, skill_id)
            );
        """.trimIndent()

        getConnection().use { conn ->
            conn.createStatement().use { st -> st.execute(sql) }
        }
    }

    fun saveParsedData(students: StudentList) {
        val insertStudentSql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)"
        val selectSkillSql = "SELECT id FROM skills WHERE skill_name = ?"
        val insertSkillSql = "INSERT INTO skills (skill_name, is_hard, is_soft) VALUES (?, ?, ?)"
        val bindSkillToStudentSql = "INSERT INTO student_to_skills (student_id, skill_id) VALUES (?, ?) ON CONFLICT DO NOTHING"

        getConnection().use { connection ->
            connection.autoCommit = false
            connection.prepareStatement(insertStudentSql, Statement.RETURN_GENERATED_KEYS).use { studentStmt ->
                connection.prepareStatement(selectSkillSql).use { selectSkillStmt ->
                    connection.prepareStatement(insertSkillSql, Statement.RETURN_GENERATED_KEYS).use { insertSkillStmt ->
                        connection.prepareStatement(bindSkillToStudentSql).use { bindStmt ->

                            for (student in students.students) {
                                studentStmt.setString(1, student.firstName)
                                studentStmt.setString(2, student.secondName)
                                studentStmt.executeUpdate()

                                val studentId = studentStmt.generatedKeys.let { if (it.next()) it.getInt(1) else throw Exception("No ID") }

                                for (skill in student.skills) {
                                    var skillId: Int? = null

                                    selectSkillStmt.setString(1, skill.name)
                                    selectSkillStmt.executeQuery().use { rs ->
                                        if (rs.next()) {
                                            skillId = rs.getInt("id")
                                        }
                                    }

                                    if (skillId == null) {
                                        insertSkillStmt.setString(1, skill.name)
                                        insertSkillStmt.setBoolean(2, skill.hard)
                                        insertSkillStmt.setBoolean(3, skill.soft)
                                        insertSkillStmt.executeUpdate()

                                        insertSkillStmt.generatedKeys.use { rs ->
                                            if (rs.next()) skillId = rs.getInt(1)
                                        }
                                    }

                                    if (skillId != null) {
                                        bindStmt.setInt(1, studentId)
                                        bindStmt.setInt(2, skillId!!)
                                        bindStmt.addBatch()
                                    }
                                }
                                bindStmt.executeBatch()
                            }
                        }
                    }
                }
            }
            connection.commit()
        }
    }
}