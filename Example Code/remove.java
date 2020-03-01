public bool Remove(T item) {
    SkipListNode cur = head;

    bool found = false;

    for (int level = depth – 1; level >= 0; level–-) {
        for (; cur.Next[level] != null; cur = cur.Next[level].Value) {
            if (cur.Next[level].Value.Item.CompareTo(item) == 0) {
                found = true;
                cur.Next[level] = cur.Next[level].Value.Next[level];
                count–;
                break;
            }

            if (cur.Next[level].Value.Item.CompareTo(item) > 0) {
                break;
            }
        }
    }

    return found;
}