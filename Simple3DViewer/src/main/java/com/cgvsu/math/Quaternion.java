package com.cgvsu.math;

public class Quaternion extends Vector4f {
    // забавный факт, по факту, в теории, я мог использовать класс vector4f

    public Quaternion(float w, float x, float y, float z) {
        super(w, x, y, z);
    }

    /**
     *  <p>
     *     Так, отлично, теперь разберемся как перемножать 2 кватерниона с действительной и мнимой частями:<br>
     *     p = p<sub>1</sub> + p<sub>0</sub><br>
     *     q = q<sub>1</sub> + q<sub>0</sub><br>
     *     Тогда, найдем pq;<br>
     *      pq = (p<sub>1</sub> + p<sub>0</sub>)(q<sub>1</sub> + q<sub>0</sub>) = p<sub>1</sub>q<sub>1</sub>+
     *      p<sub>1</sub>q<sub>0</sub> + p<sub>0</sub>q<sub>1</sub> + p<sub>0</sub>q<sub>0</sub> <br>
     *      Заметим, что все у нас хорошо, мы имеем отличное произведение везде, кроме последней части,
     *      там перемножение 2х кватернионов без действительной части, а как мы знаем это равно pq = [p*q]-'p;q' <br>
     *      Тогда окончательно эта хрень имеет вид: <br>
     *      pq = p<sub>1</sub>q<sub>1</sub> + p<sub>1</sub>q<sub>0</sub> + p<sub>0</sub>q<sub>1</sub>
     *      + ([p<sub>0</sub>*q<sub>0</sub>] - 'p<sub>0</sub>;q<sub>0</sub>') <br>
     *      заметим, что у нас числа получились тут: p<sub>1</sub>q<sub>1</sub>, 'p<sub>0</sub>;q<sub>0</sub>' => все остальное вектора.
     * </p>
     * @param other 2й кватернион (перемножается справа)
     */

    public void multiply(Quaternion other) {
        float w1 = this.getX();
        float x1 = this.getY();
        float y1 = this.getZ();
        float z1 = this.getW();

        float w2 = other.getX();
        float x2 = other.getY();
        float y2 = other.getZ();
        float z2 = other.getW();

        // Кватернионное умножение
        float newW = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
        float newX = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2;
        float newY = w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2;
        float newZ = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2;

        this.setX(newW);
        this.setY(newX);
        this.setZ(newY);
        this.setW(newZ);
    }

    @Override
    public void normalize() {
        super.normalize();
    }
    // По-честному, надо бы все остальное прописать... но мне похер, я всегда знал, что буду злодеем :)

    @Override
    public String toString() {
        return "Quaternion{" +
                "Вущественная часть =" + getW() +
                ", i=" + getX() +
                ", j=" + getY() +
                ", k=" + getZ() +
                '}';
    }
}